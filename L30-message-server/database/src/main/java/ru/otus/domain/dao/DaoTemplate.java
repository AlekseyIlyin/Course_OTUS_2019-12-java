package ru.otus.domain.dao;

import org.bson.types.ObjectId;
import ru.otus.domain.User;

import java.util.List;

public interface DaoTemplate<T> {
  T findById(Object id, Class<T> clazz );
  List<T> findByField(String fieldName, Object fieldValue);
  List<T> findAll();
  ObjectId saveObject(T objectSource);
  void initDb();
}
