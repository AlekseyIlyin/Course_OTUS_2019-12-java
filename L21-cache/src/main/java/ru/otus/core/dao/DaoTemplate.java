package ru.otus.core.dao;

import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface DaoTemplate<T> {
  Optional<T> findById(long id, Class<T> clazz );

  long saveObject(T objectSource);

  SessionManager getSessionManager();
}
