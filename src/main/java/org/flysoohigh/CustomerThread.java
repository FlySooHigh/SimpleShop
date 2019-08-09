package org.flysoohigh;

import org.flysoohigh.service.ImportDataService;
import org.flysoohigh.service.command.*;
import org.flysoohigh.service.login.ILoginService;
import org.flysoohigh.service.login.LoginService;
import org.flysoohigh.service.login.LogoutService;
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
//    @Autowired
//    private ViewShopService viewShopService;

    // FIXME: 04.08.2019 Сделать в виде констант с состояниями ? Или в виде енума ?
    private List<String> availableCommands = Arrays.asList("login", "logout", "viewshop", "myinfo", "buy", "sell", "exit");
    private ImportDataService dataService;
    private String currentUser = "";

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
            ICommandService viewShopService = new ViewShopService(out, dataService);
            ILoginService loginService = new LoginService(out, dataService);
            ILoginService logoutService = new LogoutService(out, dataService);
            ICommandService myInfoService = new MyInfoService(out, dataService);
            ICommandService buyService = new BuyService(out, dataService);
            ICommandService sellService = new SellService(out, dataService);

            String inputLine, command, parameter;
            boolean keepGoing = true;

            out.println("Welcome to the SimpleShop!");

            // FIXME: 08.08.2019 Не забыть переключиться на in-memory DB, написать тест!
            while (keepGoing && (inputLine = in.readLine()) != null) {

                inputLine = clear(inputLine);

                String[] parsedCommand = inputLine.split(" ");
                if (parsedCommand.length > 2) {
                    out.println("Command should consist of 2 words maximum");
                }

                if (!availableCommands.contains(parsedCommand[0])) {
                    out.println("Command is not in the list, try again");
                }

                switch (parsedCommand[0]){
                    case "login":
                        currentUser = loginService.handleInput(parsedCommand, currentUser);
                        break;
                    case "logout":
                        currentUser = logoutService.handleInput(parsedCommand, currentUser);
                        break;
                    case "myinfo":
                        myInfoService.handleInput(parsedCommand, currentUser);
                        break;
                    case "viewshop":
                        viewShopService.handleInput(parsedCommand, currentUser);
                        break;
                    case "buy":
                        buyService.handleInput(parsedCommand, currentUser);
                        break;
                    case "sell":
                        sellService.handleInput(parsedCommand, currentUser);
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
