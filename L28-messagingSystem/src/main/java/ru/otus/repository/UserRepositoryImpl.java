package ru.otus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private static final String COLLECTION_NAME_USER = "users";

    private final MongoOperations mongoOperations;

    @Autowired
    public UserRepositoryImpl(MongoOperations mongoOperations) {
      this.mongoOperations = mongoOperations;
    }

    @Override
    public List<User> findAll() {
      return mongoOperations.findAll(User.class,COLLECTION_NAME_USER);
    }

    @Override
    public String create(String name, String login, String password) {
      return create(new User(name,login,password));
    }

    @Override
    public String create(User user) {
        mongoOperations.save(user, COLLECTION_NAME_USER);
        return user.getId();
    }

}
