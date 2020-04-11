package ru.otus.l10.jdbc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l10.core.dao.UserDaoTemplate;
import ru.otus.l10.jdbc.DbExecutor;
import ru.otus.l10.core.sessionmanager.SessionManager;
import ru.otus.l10.jdbc.sessionmanager.SessionManagerJdbc;

public class ObjectDaoJdbc<T> implements UserDaoTemplate<T> {
  private static Logger logger = LoggerFactory.getLogger(ObjectDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<T> dbExecutor;
  private SqlQueryBuilder<T> sqlQueryBuilder;
  private final Class<T> tClass;

  public ObjectDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, Class<T> tClass) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.tClass = tClass;
      sqlQueryBuilder = new SqlQueryBuilder<T>(tClass);
  }

  @Override
  public Optional<T> findById(long id, Class<T> clazz) {
    Map<String, Object> queryValues = sqlQueryBuilder.getQueryValues();
    try {
      dbExecutor.selectRecord(getConnection(), sqlQueryBuilder.getSelectQuery(), id, resultSet -> {
        try {
          if (resultSet.next()) {
            for (String s : sqlQueryBuilder.getQueryValues().keySet()) {
              queryValues.put(s, resultSet.getObject(s));
            }
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return null;
      });

      return Optional.of(new ObjectMaker<T>(tClass, queryValues).createObject());

    } catch (Exception e) {
      e.fillInStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ObjectDaoJdbc{");
    sb.append("Class=").append(tClass);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public void create(T objectSource) {
    try {
      sqlQueryBuilder.setObjectForQueryBuild(objectSource);
      final var insertQuery = sqlQueryBuilder.getInsertQuery();
      dbExecutor.insertRecord(getConnection(), insertQuery, sqlQueryBuilder.getParams());
    } catch (Exception e) {
      throw new ObjectDaoException(e);
    }
  }

  @Override
  public void update(T objectSource) {
    try {
      sqlQueryBuilder.setObjectForQueryBuild(objectSource);
      final var updateQuery = sqlQueryBuilder.getUpdateQuery();
      dbExecutor.updateRecord(getConnection(), updateQuery, sqlQueryBuilder.getParams());
    } catch (Exception e) {
      throw new ObjectDaoException(e);
    }
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }

  private Connection getConnection() {
    return sessionManager.getCurrentSession().getConnection();
  }

}
