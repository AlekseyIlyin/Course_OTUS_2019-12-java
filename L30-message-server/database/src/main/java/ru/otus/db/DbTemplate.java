package ru.otus.db;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface DbTemplate {
    <T> ObjectId insert(T value);
    <T> List<T> find(String fieldName, Object fieldValue, Class<T> tClass) throws Exception;
    <T> List<T> findAll(Class<T> tClass) throws Exception;
    void dropTable();
}
