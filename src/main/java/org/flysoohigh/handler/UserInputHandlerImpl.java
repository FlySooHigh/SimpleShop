package org.flysoohigh.handler;

import org.flysoohigh.repository.CustomerRepo;
import org.flysoohigh.service.ImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Deprecated
@Component
public class UserInputHandlerImpl implements UserInputHandler {
    @Autowired
    private ImportDataService importDataService;

    @Autowired
    private CustomerRepo customerRepo;

    List<String> availableCommands = Arrays.asList("login", "logout", "viewshop", "myinfo", "buy", "sell", "exit");

    public String handleUserInput(String inputLine) {
        String[] parsedInput = parseIntoWords(inputLine);

        String result = "ok";

        if (!availableCommands.contains(parsedInput[0])) {
            return "Command is not in the list, try again";
        }

        if (parsedInput[0].equals("login")) {
            // FIXME: 04.08.2019 Тут будет NPE, если не проверять параметры
            boolean isCustomer = importDataService.isCustomer(parsedInput[1]);
        }

        if (parsedInput[0].equals("exit")) {
            return "exit";
        }


        return result;
    }

    private String[] parseIntoWords(String inputLine) {
        return inputLine.split(" ");
    }
}
