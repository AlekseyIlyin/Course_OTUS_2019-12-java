package ru.otus.l10.core.dao;

public class DaoException extends RuntimeException {
  public DaoException(Exception ex) {
    super(ex);
  }
}
