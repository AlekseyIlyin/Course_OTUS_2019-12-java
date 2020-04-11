package ru.otus.l10.core.service;

import java.util.Optional;

public interface DBService<T> {

  void saveObject (T objectSource);

  void updateObject (T objectSource);

  Optional<T> getUser(long id, Class<T> clazz);

}
