package ru.otus.dao;

import org.bson.types.ObjectId;

import java.util.Optional;

public interface DaoTemplate<T> {
  Optional<T> findById(ObjectId id, Class<T> clazz );
  Optional<T> findByField(String fieldName, Object fieldValue);
  ObjectId saveObject(T objectSource);
}
