package org.flysoohigh.service.command;

public interface ICommandService {
    void handleInput(String[] parsedCommand, String loggedInCustomer);
}
