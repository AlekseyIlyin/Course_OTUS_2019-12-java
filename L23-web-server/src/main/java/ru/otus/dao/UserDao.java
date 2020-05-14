package ru.otus.dao;

import org.bson.types.ObjectId;
import ru.otus.db.MongoTemplate;
import ru.otus.model.User;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class UserDao implements DaoTemplate<User>{

    private final MongoTemplate mongoTemplate;

    public UserDao(MongoTemplate mongoClient) {
        this.mongoTemplate = mongoClient;
    }

    @Override
    public Optional<User> findById(ObjectId id, Class<User> clazz) {
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = mongoTemplate.findOne(eq("_id", id), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByField(String fieldName, Object fieldValue) {
        Optional<User> optionalUser = Optional.empty();
        User user = null;
        try {
            optionalUser = mongoTemplate.findOne(eq(fieldName, fieldValue), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalUser;
    }

    public User findById(ObjectId id) {
        Optional<User> optionalUser = findById(id, User.class);
        return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    public User findByLogin(String login) {
        Optional<User> optionalUser = findByField("login",login);
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

    @Override
    public ObjectId saveObject(User user) {
        ObjectId id = mongoTemplate.insert(user);
        user.set_id(id);
        return id;
    }
}
