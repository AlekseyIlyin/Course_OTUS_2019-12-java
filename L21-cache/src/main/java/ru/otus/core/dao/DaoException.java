package ru.otus.core.dao;

public class DaoException extends RuntimeException {
  public DaoException(Exception ex) {
    super(ex);
  }
}
