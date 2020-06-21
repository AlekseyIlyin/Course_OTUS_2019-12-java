package ru.otus.domain.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import ru.otus.db.DbTemplate;
import ru.otus.domain.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserDao implements DaoTemplate<User>{

    private final DbTemplate dbTemplateImpl;

    @Override
    public User findById(Object id, Class<User> clazz) {
        List<User> users = findByField("id", id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findByField(String fieldName, Object fieldValue) {
        List<User> users = new ArrayList<>();
        try {
            users = dbTemplateImpl.find(fieldName, fieldValue, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User findByLogin(String login) {
        List<User> users = findByField("login",login);
        return users.isEmpty() ? null : users.get(0);
    }

    public List<User> findAll() {
        List<User> users = null;
        try {
            users = dbTemplateImpl.findAll(User.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error for load all {}", e);
        }
        return users;
    }

    @Override
    public ObjectId saveObject(User user) {
        ObjectId id = dbTemplateImpl.insert(user);
        user.setId(id);
        return id;
    }

    @Override
    public void initDb() {
        dbTemplateImpl.dropTable();
/*
        User adminUser = findByLogin("admin");
        if (adminUser == null) {
*/
            saveObject(new User("Admin","admin","11111"));
        //}
    }
}
