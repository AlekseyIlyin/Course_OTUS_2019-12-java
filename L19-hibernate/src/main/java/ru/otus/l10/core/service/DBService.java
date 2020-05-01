package ru.otus.l10.core.service;

import java.util.Optional;

public interface DBService<T> {

  long saveObject (T object);

  Optional<T> getObject(long id, Class<T> clazz);

}
