package ru.otus.repostory;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class UserRepositoryImpl implements UserRepository{
    final String COLLECTION_NAME_USER = "users";

    private final MongoTemplate mongoOperations; // = new MongoTemplate(new SimpleMongoClientDatabaseFactory());

    private final List<User> users = new ArrayList<>();

    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
      mongoOperations = mongoTemplate;
      mongoOperations.dropCollection(COLLECTION_NAME_USER);
      mongoOperations.save(new User("Admin","admin", "11111"),COLLECTION_NAME_USER);
    }


  @Override
    public List<User> findAll() {
        //return users;
      return mongoOperations.findAll(User.class,COLLECTION_NAME_USER);
    }

  @Override
  public String create(String name, String login, String password) {
      User user = new User(name,login,password);
      mongoOperations.save(user, COLLECTION_NAME_USER);
      return user.getId();
  }

}
