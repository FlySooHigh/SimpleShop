package org.flysoohigh;

import org.flysoohigh.service.ImportDataService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerThread {

    public static void main(String[] args) throws JAXBException {

        if (args.length != 1) {
            System.err.println("Usage: java ServerThread <port number>");
            System.exit(1);
        }

        ClassPathXmlApplicationContext serverContext = new ClassPathXmlApplicationContext("classpath:/spring.xml");
        ImportDataService dataService = serverContext.getBean(ImportDataService.class);
        dataService.saveItems(dataService.parseItemsXml());
        dataService.saveSomeCustomers();

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("ServerSocket started");
            // FIXME: 09.08.2019 Правильное использование экзекьютора ? :)
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            while (listening) {
                CustomerThread customerThread = new CustomerThread(serverSocket.accept(), serverContext);
                executor.execute(customerThread);
                System.out.println("New customer connected");
            }
            System.out.println("ServerSocket stopped");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(1);
        }
    }


}
