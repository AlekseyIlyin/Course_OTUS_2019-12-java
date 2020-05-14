package ru.otus.services;

import ru.otus.dao.UserDao;
import ru.otus.model.User;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        User findUser = userDao.findByLogin(login);
        return findUser != null && findUser.getPassword().equals(password);
    }

}
