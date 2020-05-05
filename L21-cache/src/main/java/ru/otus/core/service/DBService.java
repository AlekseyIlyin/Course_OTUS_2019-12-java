package ru.otus.core.service;

import java.util.Optional;

public interface DBService<T> {

  long saveObject (T object);

  Optional<T> getObject(long id, Class<T> clazz);

}
