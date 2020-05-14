package ru.otus.db;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("База MongoDB должна ")
class MongoTest {
    private static final String MONGODB_URL = "mongodb://localhost"; // Работа без DockerToolbox
    //private static final String MONGODB_URL = "mongodb://192.168.99.100"; // Работа через DockerToolbox
    private static final String MONGO_DATABASE_NAME = "mongo-db-test";
    private static final String USERS_TABLE_NAME = "users";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoClient = MongoClients.create(MONGODB_URL);
        val mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        database = mongoClient.getDatabase(MONGO_DATABASE_NAME);
        val collection = database.getCollection(USERS_TABLE_NAME);
        mongoTemplate = new MongoTemplateImpl(collection, mapper);
        database.drop();
    }

    @DisplayName(" корректно добавлять значения и получать их по ID")
    @Test
    void insert() {
        val user = getDefaultUser();
        val id = mongoTemplate.insert(user);
        user.set_id(id);
        val userCompare1 = getDefaultUser();
        Optional<User> userCompare2 = null;
        try {
            userCompare2 = mongoTemplate.findOne(eq("_id", id), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(userCompare2).isPresent().get().isEqualToComparingOnlyGivenFields(userCompare1,"name","age","address"); //  .isEqualToComparingFieldByField(expectedUser);
        assertThat(((User)userCompare2.get()).getPhones()).containsExactlyInAnyOrderElementsOf(userCompare1.getPhones());
    }

    @DisplayName(" корректно находить значения по свойствам")
    @Test
    void find() {
        val user = getDefaultUser();
        val id = mongoTemplate.insert(user);
        user.set_id(id);
        val userCompare1 = getDefaultUser();
        Optional<User> userCompare2 = null;
        try {
            userCompare2 = mongoTemplate.findOne(eq("name", user.getName()), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(userCompare2).isPresent().get().isEqualToComparingOnlyGivenFields(userCompare1,"name","age","address"); //  .isEqualToComparingFieldByField(expectedUser);
        assertThat(((User)userCompare2.get()).getPhones()).containsExactlyInAnyOrderElementsOf(userCompare1.getPhones());
    }

    @DisplayName(" корректно получать все значения")
    @Test
    void findAll() {
        List<User> userList1 = new ArrayList<>(3);
        for (int i = 1; i <=3; i++) {
            User user = getDefaultUser();
            user.setAge(user.getAge() + i);
            val id = mongoTemplate.insert(user);
            user.set_id(id);
            userList1.add(user);
        }

        List<User> usersFromDB = new ArrayList<>(0);
        try {
           usersFromDB = mongoTemplate.findAll(User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(userList1.size() == usersFromDB.size());
    }

    private User getDefaultUser() {
        return new User("Aleksey",45, "Smolensk, Nikolaeva st.", "+79993334455");
    }
}