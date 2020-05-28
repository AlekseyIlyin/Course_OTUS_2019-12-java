package ru.otus.repository;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;

@Service
public class MongoInitDb {

    private static final String COLLECTION_NAME_USER = "users";

    private final MongoOperations mongoOperations;

    public MongoInitDb(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public void init() {
        this.mongoOperations.dropCollection(COLLECTION_NAME_USER);
        this.mongoOperations.save(new User("Admin","admin", "11111"),COLLECTION_NAME_USER);
    }
}
