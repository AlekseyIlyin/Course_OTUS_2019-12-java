package ru.otus.l10.core.service;

public class DbServiceException extends RuntimeException {
  public DbServiceException(Exception e) {
    super(e);
  }
}
