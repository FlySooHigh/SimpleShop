package org.flysoohigh;

import org.flysoohigh.config.Config;
import org.flysoohigh.service.ImportDataService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerThread {

    public static void main(String[] args) throws JAXBException {
        if (args.length != 1) {
            System.err.println("Usage: java ServerThread <port number>");
            System.exit(1);
        }

        AnnotationConfigApplicationContext serverContext = new AnnotationConfigApplicationContext(Config.class);

        ImportDataService dataService = serverContext.getBean(ImportDataService.class);
        // вычитываем items.xml и сохраняем предметы в БД
        dataService.saveItems(dataService.parseItemsXml());
        // сохраним несколько юзеров, чтобы было кем логиниться
        dataService.saveSomeCustomers();

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("ServerSocket started");
            while (listening) {
                new CustomerThread(serverSocket.accept(), serverContext).start();
                System.out.println("New customer connected");
            }
            System.out.println("ServerSocket stopped");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(1);
        }
    }
}
