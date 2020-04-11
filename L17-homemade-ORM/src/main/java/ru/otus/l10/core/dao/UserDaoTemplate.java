package ru.otus.l10.core.dao;

import ru.otus.l10.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDaoTemplate<T> {
  Optional<T> findById(long id, Class<T> clazz );

  void create(T objectSource);

  void update(T objectSource);

  SessionManager getSessionManager();
}
