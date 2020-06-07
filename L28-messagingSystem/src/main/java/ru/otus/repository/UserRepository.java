package ru.otus.repository;

import ru.otus.domain.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    String create(String name, String login, String password);

    String create(User user);
}
