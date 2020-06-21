package ru.otus;

import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;

public class MessageServer {
    public static void main(String[] args) {
        new MessageServer().startServer();
    }

    public void startServer() {
        MessageSystem messageSystem = new MessageSystemImpl();
    }
}
