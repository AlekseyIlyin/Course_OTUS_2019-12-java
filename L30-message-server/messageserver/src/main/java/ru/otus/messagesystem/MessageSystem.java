package ru.otus.messagesystem;

import java.util.UUID;

public interface MessageSystem {

    void addClient(MessageSystemImpl.Client client, String clientType, UUID clientId);

    void removeClient(String clientType, UUID clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;
}

