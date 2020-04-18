package ru.otus.l10.jdbc.dao;

public class ObjectDaoException extends RuntimeException {
  public ObjectDaoException(Exception ex) {
    super(ex);
  }
}
