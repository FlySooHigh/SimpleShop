package org.flysoohigh;

import org.flysoohigh.service.ImportDataService;
import org.flysoohigh.service.command.BuyService;
import org.flysoohigh.service.command.MyInfoService;
import org.flysoohigh.service.command.SellService;
import org.flysoohigh.service.command.ViewShopService;
import org.flysoohigh.service.login.LoginService;
import org.flysoohigh.service.login.LoginServiceImpl;
import org.flysoohigh.service.login.LogoutServiceImpl;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class CustomerThread extends Thread {

    private Socket socket;
    private ApplicationContext clientContext;

    // FIXME: 04.08.2019 Сделать в виде констант с состояниями ? Или в виде енума ?
    private List<String> availableCommands = Arrays.asList("login", "logout", "viewshop", "myinfo", "buy", "sell", "exit");
    private ImportDataService dataService;
    private String loggedInCustomer = "";

    public CustomerThread(Socket socket, ApplicationContext context) {
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
            // FIXME: 08.08.2019 Попробовать засунуть в бины?
            ViewShopService viewShopService = new ViewShopService(out, dataService);
            LoginService loginService = new LoginServiceImpl(out, dataService);
            LogoutServiceImpl logoutService = new LogoutServiceImpl(out, dataService);
            MyInfoService myInfoService = new MyInfoService(out, dataService);
            BuyService buyService = new BuyService(out, dataService);
            SellService sellService = new SellService(out, dataService);

            String inputLine, command, parameter;
            boolean keepGoing = true;

            out.println("Welcome to the SimpleShop!");

            // FIXME: 08.08.2019 Не забыть переключиться на in-memory DB, написать тест!
            while (keepGoing && (inputLine = in.readLine()) != null) {

                inputLine = removeGarbageSymbols(inputLine);

                String[] parsedCommand = inputLine.split(" ");
                if (parsedCommand.length > 2) {
                    out.println("Command should consist of 2 words maximum");
                }

                if (!availableCommands.contains(parsedCommand[0])) {
                    out.println("Command is not in the list, try again");
                }

                switch (parsedCommand[0]){
                    case "login":
                        loggedInCustomer = loginService.handleInput(parsedCommand, loggedInCustomer);
                        break;
                    case "logout":
                        loggedInCustomer = logoutService.handleInput(parsedCommand, loggedInCustomer);
                        break;
                    case "myinfo":
                        myInfoService.handleInput(parsedCommand, loggedInCustomer);
                        break;
                    case "viewshop":
                        viewShopService.handleInput(parsedCommand);
                        break;
                    case "buy":
                        buyService.handleInput(parsedCommand, loggedInCustomer);
                        break;
                    case "sell":
                        sellService.handleInput(parsedCommand, loggedInCustomer);
                        break;
                    case "exit":
                        keepGoing = false;
                }
            }
            out.println("Socket will be closed");
            Thread.sleep(2000);
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // логаутим юзера из БД, чтобы он не остался залогиненным при закрытии окна пользовательской сессии без команды logout
        finally {
            dataService.logOut(loggedInCustomer);
        }
    }

    private String removeGarbageSymbols(String inputLine) {
        return inputLine.replaceAll("[^\\w\\s]", "").trim();
    }
}
