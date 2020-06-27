package ru.otus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.val;
import ru.otus.db.DbTemplate;
import ru.otus.db.MongoTemplateImpl;
import ru.otus.domain.dao.DaoTemplate;
import ru.otus.domain.dao.UserDao;
import ru.otus.exceptions.ParametersLoadException;
import ru.otus.handlers.UserRequestHandler;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.messagesystem.RequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class DatabaseClient {
    private static final String CONFIG_NAME = "app.properties";
    private static final int PORT = 53333; //8080;
    private static final String HOST = "localhost";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private final Properties properties;

    private MongoClient mongoClient;
    private MongoDatabase database;

    private DbTemplate dbTemplateImpl;
    private DaoTemplate userDao;


    public DatabaseClient() throws Exception{
        try {
            this.properties = getProperties();
        } catch (ParametersLoadException e) {
            throw new Exception();
        }
    }

    public static void main(String[] args) {
        try {
            new DatabaseClient().connectToMessageServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDbService(Serializer serializer, Socket messageServer) {
        mongoClient = MongoClients.create(properties.getProperty("mongo.url"));
        val mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        database = mongoClient.getDatabase(properties.getProperty("mongo.dbName"));
        val collection = database.getCollection(properties.getProperty("mongo.tableNameUsers"));
        dbTemplateImpl = new MongoTemplateImpl(collection, mapper);
        userDao = new UserDao(dbTemplateImpl);
        userDao.initDb();

        var databaseMsClient = new MsClientImpl(serializer, DATABASE_SERVICE_CLIENT_NAME, messageServer, this);

        RequestHandler usersRequestHandler = new UserRequestHandler(userDao, serializer);
        databaseMsClient.addHandler(MessageType.USER_DATA, usersRequestHandler);
        databaseMsClient.addHandler(MessageType.USER_CREATE, usersRequestHandler);
        databaseMsClient.startListening();
    }

    public void connectToMessageServer() {
        try {
            Socket clientSocket = new Socket(HOST, PORT);
            initDbService(new Serializer(), clientSocket);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private static Properties getProperties() throws ParametersLoadException {
        ClassLoader classLoader = DatabaseClient.class.getClassLoader();
        Properties properties = new Properties();
        File file = new File(classLoader.getResource(CONFIG_NAME).getFile());
        if (file.exists() && file.canRead()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                properties.load(fileInputStream);
            } catch (IOException e) {
                throw new ParametersLoadException(e.getMessage());
            }
        } else {
            throw new ParametersLoadException("File doesn't may read");
        }

        return properties;
    }

}
