package ru.otus.repostory;

import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long saveUser(User object);

    Optional<User> getUser(long id);

    List<User> getAll();

    Optional<User> findByLogin(String login);
}
