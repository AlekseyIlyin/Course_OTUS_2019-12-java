package ru.otus.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.Serializer;
import ru.otus.front.FrontendService;
import ru.otus.front.UsersResponseHandler;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

@Configuration
@Slf4j
public class FrontendClientBeanConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    private static final int PORT = 53333; //8080;
    private static final String HOST = "localhost";
    
    @Bean
    public String getDatabaseServiceClientName() {
        return DATABASE_SERVICE_CLIENT_NAME;
    }

    @Bean(name = "clientSocket")
    public Socket createMessageSystemSocket() {
        try {
            return new Socket(HOST, PORT);
        } catch (IOException e) {
            return null;
        }
    }

    @Bean(name = "frontendMsClient")
    public MsClient createFrontClient(Serializer serializer, FrontendService frontendService, Socket clientSocket) {

        MsClient frontendMsClient = new MsClientImpl(serializer, FRONTEND_SERVICE_CLIENT_NAME, clientSocket);

        final var userResponseHandler = new UsersResponseHandler(serializer, frontendService);
        frontendMsClient.addHandler(MessageType.USER_DATA, userResponseHandler);
        frontendMsClient.addHandler(MessageType.USER_CREATE, userResponseHandler);


        return frontendMsClient;
    }
}
