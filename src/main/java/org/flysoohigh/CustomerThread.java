package org.flysoohigh;

import org.flysoohigh.model.Item;
import org.flysoohigh.service.ImportDataService;
import org.flysoohigh.service.buy.IBuyService;
import org.flysoohigh.service.login.ILoginService;
import org.flysoohigh.service.logout.ILogoutService;
import org.flysoohigh.service.myinfo.IMyInfoService;
import org.flysoohigh.service.sell.ISellService;
import org.flysoohigh.service.viewshop.IViewShopService;
import org.flysoohigh.util.Pair;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.stream.IntStream;

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

            IViewShopService viewShopService = clientContext.getBean(IViewShopService.class);
            ILoginService loginService = clientContext.getBean(ILoginService.class);
            ILogoutService logoutService = clientContext.getBean(ILogoutService.class);
            IMyInfoService myInfoService = clientContext.getBean(IMyInfoService.class);
            IBuyService buyService = clientContext.getBean(IBuyService.class);
            ISellService sellService = clientContext.getBean(ISellService.class);

            String inputLine;
            String command = EMPTY_STRING;
            String parameter = EMPTY_STRING;

            boolean keepGoing = true;
            boolean validationPassed;

            out.println("Welcome to the SimpleShop!");

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
                            Pair<String, String> loginResult = loginService.login(parameter, currentUser);
                            currentUser = loginResult.getUser();
                            out.println(loginResult.getMessage());
                            break;
                        case "logout":
                            Pair<String, String> logoutResult = logoutService.logout(currentUser);
                            currentUser = logoutResult.getUser();
                            out.println(logoutResult.getMessage());
                            break;
                        case "myinfo":
                            out.println(myInfoService.showInfo(currentUser));
                            break;
                        case "viewshop":
                            List<Item> items = viewShopService.showItems();
                            IntStream.range(0, items.size())
                                     .forEach(i -> out.println(items.get(i).toString()));
                            break;
                        case "buy":
                            out.println(buyService.buy(parameter, currentUser));
                            break;
                        case "sell":
                            out.println(sellService.sell(parameter, currentUser));
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
