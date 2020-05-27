package ru.otus.repostory;

import ru.otus.domain.User;

import java.util.List;

public interface UserRepository {//extends MongoRepository<User, String>{

    List<User> findAll();

    String create(String name, String login, String password);
}
