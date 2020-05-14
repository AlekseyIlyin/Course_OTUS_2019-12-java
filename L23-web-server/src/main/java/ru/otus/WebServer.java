package ru.otus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.val;
import ru.otus.dao.UserDao;
import ru.otus.db.MongoTemplate;
import ru.otus.db.MongoTemplateImpl;
import ru.otus.model.User;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class WebServer {
    private static final String CONFIG_NAME = "realm.properties";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoTemplate mongoTemplate;

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = WebServer.class.getClassLoader();
        File file = new File(classLoader.getResource(CONFIG_NAME).getFile());
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        }

        mongoTemplate = getMongoTemplate(properties);
        UserDao userDao = new UserDao(mongoTemplate);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();

        mongoClient.close();
    }

    private static MongoTemplate getMongoTemplate(Properties properties) {
        String mongoURL = properties.getProperty("MongoURL");
        String mongoDbName = properties.getProperty("MongoDbName");
        String tableNameUsers = properties.getProperty("TableNameUsers");
        String tableNamePhones = properties.getProperty("TableNamePhones");

        mongoClient = MongoClients.create(mongoURL);
        val mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        database = mongoClient.getDatabase(mongoDbName);
        val collection = database.getCollection(tableNameUsers);
        mongoTemplate = new MongoTemplateImpl(collection, mapper);
        database.drop();

        mongoTemplate.insert(new User("user1", "user1", "11111"));
        mongoTemplate.insert(new User("user2", "user2", "11111"));
        mongoTemplate.insert(new User("user3", "user3", "11111"));

        return mongoTemplate;
    }
}
