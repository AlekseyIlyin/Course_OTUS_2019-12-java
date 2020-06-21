package ru.otus.messagesystem;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.otus.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MsClientImpl implements MsClient {
    private final Gson gson = new Gson();
    private final Serializer serializer;
    private final String name;
    private final UUID id;
    private final Socket messageSystem;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();
    private PrintWriter messageSystemOut;
    private BufferedReader messageSystemIn;

    public MsClientImpl(Serializer serializer, String name, Socket messageSystem) {
        this.serializer = serializer;
        this.name = name;
        this.id = UUID.randomUUID();
        this.messageSystem = messageSystem;

        try {
            messageSystemOut = new PrintWriter(messageSystem.getOutputStream(), true);
            messageSystemIn = new BufferedReader(new InputStreamReader(messageSystem.getInputStream()));
            messageSystemOut.println(gson.toJson(new Message(name, id,"server", null,null, MessageType.CONNECT.getValue(), "".getBytes())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @SneakyThrows
    @Override
    public boolean sendMessage(Message msg) {
        messageSystemOut.println(gson.toJson(msg));
        messageSystemOut.flush();
        return true;
    }

    @Override
    public void handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                log.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, id, to, null,null, msgType.getValue(), serializer.serialize(data));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void closeConnection() {
        try {
            messageSystemOut.close();
            messageSystemIn.close();
            messageSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        String serverResponse = null;
        try {
            while (!"stop".equals(serverResponse)) {
                serverResponse = messageSystemIn.readLine();
                if (serverResponse != null) {
                    try {
                        Message message = gson.fromJson(serverResponse, Message.class);
                        handle(message);
                    } catch (Exception ex) {
                        ex.fillInStackTrace();
                        continue;
                    }
                }
            }
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
