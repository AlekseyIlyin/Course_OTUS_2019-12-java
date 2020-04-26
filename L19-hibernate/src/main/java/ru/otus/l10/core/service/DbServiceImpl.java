package ru.otus.l10.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l10.core.dao.DaoTemplate;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {
  private static Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);

  private final DaoTemplate<T> objectDao;

  public DbServiceImpl(DaoTemplate<T> daoTemplate) {
    this.objectDao = daoTemplate;
  }

  @Override
  public long saveObject(T object) {
    return objectDao.saveObject(object);
  }

  @Override
  public Optional<T> getObject(long id, Class<T> clazz) {
    return objectDao.findById(id, clazz);
  }

}
