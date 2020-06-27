package ru.otus.messagesystem;

import java.util.UUID;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    UUID getId();

    <T> Message produceMessage(String to, T data, MessageType msgType);

    void closeConnection();

    void startListening();

}
