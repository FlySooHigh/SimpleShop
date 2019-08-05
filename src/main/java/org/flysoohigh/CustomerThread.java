package org.flysoohigh;

import org.flysoohigh.model.Customer;
import org.flysoohigh.service.ImportDataService;
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
            ImportDataService dataService = clientContext.getBean(ImportDataService.class);

            String inputLine;

            out.println("Welcome to the SimpleShop!");
            out.println("You have to login first...");

            while ((inputLine = in.readLine()) != null) {

                inputLine = removeGarbageSymbols(inputLine);
                // FIXME: 04.08.2019 Добавить объект Pair вместо parsedCommand? Из какой-нибудь коллекции
                String[] parsedCommand = inputLine.split(" ");

                if (!availableCommands.contains(parsedCommand[0])) {
                    out.println("Command is not in the list, try again");
                }

                if (parsedCommand[0].equals("login")) {
                    // FIXME: 04.08.2019 Тут будет NPE, если не проверять параметры
                    String loginName = parsedCommand[1];
                    if (dataService.isCustomer(loginName)) {
                        if (!loggedInCustomer.isEmpty()) {
                            out.println("Another user is already logged in");
                        } else if (!dataService.isLoggedIn(loginName)) {
                            dataService.logIn(loginName);
                            loggedInCustomer = loginName;
                            out.println("Login successful");
                        } else {
                            out.println("You are already logged in");
                        }
                    } else {
                        out.println("Such user is not registered. Enter existing user name.");
                    }
                }

                if (parsedCommand[0].equals("logout")) {
                    if (loggedInCustomer.isEmpty()) {
                        out.println("You are not logged in");
                    } else {
                        dataService.logOut(loggedInCustomer);
                        loggedInCustomer = "";
                        out.println("Logout successful");
                    }
                }

                if (parsedCommand[0].equals("myinfo")) {
                    if (loggedInCustomer.isEmpty()) {
                        out.println("You are not logged in");
                    } else {
                        Customer customerInfo = dataService.getInfo(loggedInCustomer);
                        out.println(customerInfo.toString());
                    }
                }

                if (parsedCommand[0].equals("buy")) {
                    if (loggedInCustomer.isEmpty()) {
                        out.println("You are not logged in");
                    } else {
                        // FIXME: 04.08.2019 Добавить проверку на null
                        String itemName = parsedCommand[1];
                        if (dataService.isInTheList(itemName)) {
                            int customerFunds = dataService.getCustomerFunds(loggedInCustomer);
                            int itemPrice = dataService.getItemPrice(itemName);
                            if (customerFunds >= itemPrice) {
                                // FIXME: 05.08.2019 Сейчас нельзя купить дважды один и тот же предмет !
                                dataService.buyItem(loggedInCustomer, itemName);
                                out.println("Purchase is successful!");
                            } else {
                                out.println("Sorry, not enough funds to purchase this item");
                            }
                        } else {
                            out.println("This item is not on the list");
                        }
                    }

                }

                if (parsedCommand[0].equals("sell")) {
                    if (loggedInCustomer.isEmpty()) {
                        out.println("You are not logged in");
                    } else {
                        // FIXME: 04.08.2019 Добавить проверку на null
                        String itemName = parsedCommand[1];
                        if (dataService.isInTheCustomerList(loggedInCustomer, itemName)) {
                            dataService.sellItem(loggedInCustomer, itemName);
                            out.println("Item sold successfully!");
                        } else {
                            out.println("This item is not in your bought items' list");
                        }
                    }

                }

                if (parsedCommand[0].equals("exit")) {
                    if (!loggedInCustomer.isEmpty()) {
                        dataService.logOut(loggedInCustomer);
                    }
                    break;
                }

            }
            out.println("Socket will be closed");
            Thread.sleep(2000);
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // FIXME: 05.08.2019 если что-то пошло не так, то надо логаутить юзера из БД
//        finally {
//            dataService.logOut(loggedInCustomer);
//        }
    }

    private String removeGarbageSymbols(String inputLine) {
        return inputLine.replaceAll("[^\\w\\s]", "").trim();
    }


}
