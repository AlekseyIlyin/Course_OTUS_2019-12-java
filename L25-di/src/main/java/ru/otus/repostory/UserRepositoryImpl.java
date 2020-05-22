package ru.otus.repostory;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

@Service
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final UserRepository userRepository;

    public UserRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long saveUser(User user) {
        return userRepository.saveUser(user);
    }

    @Override
    public Optional<User> getUser(long id) {
        Optional<User> userOptional = userRepository.getUser(id);
        return userOptional;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
