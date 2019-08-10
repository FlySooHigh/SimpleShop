package org.flysoohigh;

import org.flysoohigh.service.ImportDataService;
import org.flysoohigh.service.buy.BuyService;
import org.flysoohigh.service.buy.IBuyService;
import org.flysoohigh.service.login.ILoginService;
import org.flysoohigh.service.login.LoginService;
import org.flysoohigh.service.logout.ILogoutService;
import org.flysoohigh.service.logout.LogoutService;
import org.flysoohigh.service.myinfo.IMyInfoService;
import org.flysoohigh.service.myinfo.MyInfoService;
import org.flysoohigh.service.sell.ISellService;
import org.flysoohigh.service.sell.SellService;
import org.flysoohigh.service.viewshop.IViewShopService;
import org.flysoohigh.service.viewshop.ViewShopService;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CustomerThread extends Thread {

    private static final String EMPTY_STRING = "";

    private Socket socket;
    private ApplicationContext clientContext;
    private ImportDataService dataService;

    // вынесено сюда, чтобы можно было логаутить юзера в блоке finally
    private String currentUser = EMPTY_STRING;

    CustomerThread(Socket socket, ApplicationContext context) {
        super("CustomerThread");
        this.socket = socket;
        this.clientContext = context;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            dataService = clientContext.getBean(ImportDataService.class);
            IViewShopService viewShopService = new ViewShopService(out, dataService);
            ILoginService loginService = new LoginService(out, dataService);
            ILogoutService logoutService = new LogoutService(out, dataService);
            IMyInfoService myInfoService = new MyInfoService(out, dataService);
            IBuyService buyService = new BuyService(out, dataService);
            ISellService sellService = new SellService(out, dataService);

            String inputLine;
            String command = EMPTY_STRING;
            String parameter = EMPTY_STRING;

            boolean keepGoing = true;
            boolean validationPassed = false;

            out.println("Welcome to the SimpleShop!");

            // FIXME: 08.08.2019 Не забыть переключиться на in-memory DB, написать тест!
            while (keepGoing && (inputLine = in.readLine()) != null) {

                inputLine = clear(inputLine);

                String[] parsedInput = inputLine.split("\\s+");

                if (parsedInput.length > 2) {
                    out.println("Command should consist of 2 words maximum");
                    validationPassed = false;
                } else if (parsedInput.length == 2){
                    command = parsedInput[0];
                    parameter = parsedInput[1];
                    validationPassed = true;
                } else if (parsedInput.length == 1 && !parsedInput[0].isEmpty()) {
                    command = parsedInput[0];
                    parameter = EMPTY_STRING;
                    validationPassed = true;
                } else {
                    out.println("You have to enter some command");
                    validationPassed = false;
                }

                if (validationPassed) {
                    switch (command) {
                        case "login":
                            currentUser = loginService.login(parameter, currentUser);
                            break;
                        case "logout":
                            currentUser = logoutService.logout(currentUser);
                            break;
                        case "myinfo":
                            myInfoService.showInfo(currentUser);
                            break;
                        case "viewshop":
                            viewShopService.showItems();
                            break;
                        case "buy":
                            buyService.buy(parameter, currentUser);
                            break;
                        case "sell":
                            sellService.sell(parameter, currentUser);
                            break;
                        case "exit":
                            keepGoing = false;
                            break;
                        default:
                            out.println("Command is not in the list, try again");
                    }
                }
            }
            out.println("Socket will be closed");
            Thread.sleep(2000);
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // логаутим юзера из БД, чтобы он не остался залогиненным при закрытии окна пользовательской сессии
        // без команды logout
        finally {
            if (!currentUser.isEmpty()) {
                dataService.logOut(currentUser);
            }
        }
    }

    private String clear(String inputLine) {
        return inputLine.replaceAll("[^\\w\\s]", "").trim();
    }
}
