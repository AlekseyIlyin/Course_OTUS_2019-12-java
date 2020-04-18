package ru.otus.l10.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l10.core.dao.UserDaoTemplate;
import ru.otus.l10.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService {
  private static Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);

  private final UserDaoTemplate objectDao;

  public DbServiceImpl(UserDaoTemplate userDao) {
    this.objectDao = userDao;
  }

  @Override
  public void saveObject(Object objectSource) {
    try (SessionManager sessionManager = objectDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        objectDao.create(objectSource);
        sessionManager.commitSession();

        logger.info("created object: {}", objectDao);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional getUser(long id, Class clazz) {
    try (SessionManager sessionManager = objectDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        return objectDao.findById(id, clazz);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
    return Optional.empty();
  }

  @Override
  public void updateObject(Object objectSource) {
    try (SessionManager sessionManager = objectDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        objectDao.update(objectSource);

        logger.info("user: {}", objectDao);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
    }
  }

}
