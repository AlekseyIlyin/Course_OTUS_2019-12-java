package ru.otus.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.DaoException;
import ru.otus.core.dao.DaoTemplate;
import ru.otus.core.model.ModelDb;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class ObjectDaoHibernate<T> implements DaoTemplate<T> {
  private final static Logger logger = LoggerFactory.getLogger(ObjectDaoHibernate.class);
  private final SessionManagerHibernate sessionManager;
  private final boolean useCache;
  private final HwCache<String, T> cacheUser = new MyCache<>();

  public ObjectDaoHibernate(SessionManagerHibernate sessionManager, boolean useCache) {
    this.sessionManager = sessionManager;
    this.useCache = useCache;
  }

  public ObjectDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
    this.useCache = false;
  }

  private void addCacheObject(long id, T object) {
    cacheUser.put(Long.toString(id), object);
  }

  private T getObjectFromCache(long id) {
    return cacheUser.get(Long.toString(id));
  }

  @Override
  public Optional<T> findById(long id, Class<T> clazz) {
    if (useCache) {
      final var object = getObjectFromCache(id);
      if (object != null) {
        return Optional.ofNullable(object);
      }
    }
    try (Session currentSession = sessionManager.beginSession()) {
      final var object = currentSession.find(clazz, id);
      Thread.sleep(500); // emulate real db
      sessionManager.commitSession();
      if (useCache) {
        addCacheObject(id, object);
      }
      return Optional.ofNullable(object);
    } catch (Exception e) {
      sessionManager.rollbackSession();
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public long saveObject(T object) {
    ModelDb objectUser = (ModelDb) object;
    try (Session hibernateSession = sessionManager.beginSession()) {
      if (objectUser.getId() > 0) {
        hibernateSession.merge(object);
      } else {
        hibernateSession.persist(object);
      }
      sessionManager.commitSession();
      if (useCache) {
        addCacheObject(objectUser.getId(), object);
      }
    } catch (Exception e) {
      sessionManager.rollbackSession();
      throw new DaoException(e);
    }
    return objectUser.getId();
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
