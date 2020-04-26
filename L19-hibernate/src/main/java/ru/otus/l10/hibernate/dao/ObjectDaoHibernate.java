package ru.otus.l10.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l10.core.dao.DaoTemplate;
import ru.otus.l10.core.dao.DaoException;
import ru.otus.l10.core.model.ModelDb;
import ru.otus.l10.core.sessionmanager.SessionManager;
import ru.otus.l10.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.l10.hibernate.sessionmanager.SessionManagerHibernate;
import java.util.Optional;

public class ObjectDaoHibernate<T> implements DaoTemplate<T> {
  private static Logger logger = LoggerFactory.getLogger(ObjectDaoHibernate.class);
  private final SessionManagerHibernate sessionManager;

  public ObjectDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<T> findById(long id, Class<T> clazz) {
    sessionManager.beginSession();
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      final var object = currentSession.getHibernateSession().find(clazz, id);
      sessionManager.commitSession();
      return Optional.ofNullable(object);
    } catch (Exception e) {
      sessionManager.rollbackSession();
      logger.error(e.getMessage(), e);
    } finally {
      sessionManager.close();
    }
    return Optional.empty();
  }

  @Override
  public long saveObject(T object) {
    sessionManager.beginSession();
    ModelDb objectUser = (ModelDb) object;
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      if (objectUser.getId() > 0) {
        hibernateSession.merge(object);
      } else {
        hibernateSession.persist(object);
      }
      sessionManager.commitSession();
    } catch (Exception e) {
      sessionManager.rollbackSession();
      throw new DaoException(e);
    } finally {
      sessionManager.close();
    }
    return objectUser.getId();
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
