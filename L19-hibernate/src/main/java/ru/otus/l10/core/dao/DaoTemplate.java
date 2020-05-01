package ru.otus.l10.core.dao;

import ru.otus.l10.core.model.ModelDb;
import ru.otus.l10.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface DaoTemplate<T> {
  Optional<T> findById(long id, Class<T> clazz );

  long saveObject(T objectSource);

  SessionManager getSessionManager();
}
