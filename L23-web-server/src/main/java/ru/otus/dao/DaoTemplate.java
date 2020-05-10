package ru.otus.dao;

import org.bson.types.ObjectId;

import java.util.Optional;

public interface DaoTemplate<T> {
  Optional<T> findById(long id, Class<T> clazz );
  Optional<T> findByLogin(String login);
  ObjectId saveObject(T objectSource);
}
