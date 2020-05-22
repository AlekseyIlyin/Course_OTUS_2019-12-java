package ru.otus.hibernate;

import ru.otus.domain.User;
import org.springframework.stereotype.Service;
import ru.otus.repostory.UserRepository;

@Service
public class DbCreater {
    private final UserRepository userRepository;

    public DbCreater(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void init() {
        User user = new User("user1", "user","11111");
        userRepository.saveUser(user);
    }
}
