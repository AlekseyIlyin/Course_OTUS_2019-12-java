package ru.otus.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.val;
import org.bson.types.ObjectId;
import ru.otus.db.MongoTemplate;
import ru.otus.db.MongoTemplateImpl;
import ru.otus.model.User;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class UserDao {

    private static final String MONGODB_URL = "mongodb://localhost"; // Работа без DockerToolbox
    //private static final String MONGODB_URL = "mongodb://192.168.99.100"; // Работа через DockerToolbox
    private static final String MONGO_DATABASE_NAME = "mongo-db-test";
    private static final String USERS_TABLE_NAME = "users";
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoTemplate mongoTemplate;

    public UserDao() {
        mongoClient = MongoClients.create(MONGODB_URL);
        val mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        database = mongoClient.getDatabase(MONGO_DATABASE_NAME);
        val collection = database.getCollection(USERS_TABLE_NAME);
        mongoTemplate = new MongoTemplateImpl(collection, mapper);
        database.drop();

        mongoTemplate.insert(new User("user1", "user1", "11111"));
        mongoTemplate.insert(new User("user2", "user2", "11111"));
        mongoTemplate.insert(new User("user3", "user3", "11111"));
    }

    public User findById(ObjectId id) {
        Optional<User> optionalUser = Optional.empty();
        User user = null;
        try {
            optionalUser = mongoTemplate.findOne(eq("_id", id), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    public User findByLogin(String login) {
        Optional<User> optionalUser = Optional.empty();
        User user = null;
        try {
            optionalUser = mongoTemplate.findOne(eq("login", login), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    public List<User> findAll() {
        List<User> users = null;
        try {
            users = mongoTemplate.findAll(User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User findRandomUser() {
        List<User> users = findAll();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public ObjectId saveObject(User user) {
        ObjectId id = mongoTemplate.insert(user);
        user.set_id(id);
        return id;
    }
}
