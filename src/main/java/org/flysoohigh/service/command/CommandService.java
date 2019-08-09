package org.flysoohigh.service.command;

public interface CommandService {
    default void handleInput(String[] parsedCommand){};
    default void handleInput(String[] parsedCommand, String loggedInCustomer){};
}
