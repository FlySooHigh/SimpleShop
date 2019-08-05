package org.flysoohigh.handler;

import org.springframework.stereotype.Component;

@Deprecated
@Component
public interface UserInputHandler {
    String handleUserInput(String userInput);
}
