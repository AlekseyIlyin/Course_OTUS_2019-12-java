package ru.otus.l10.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbExecutor<T> {
  private static Logger logger = LoggerFactory.getLogger(DbExecutor.class);

  public long insertRecord(Connection connection, String sql, Queue<String> params) throws SQLException {
    Savepoint savePoint = connection.setSavepoint("savePointName");
    try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int idx = 1;
      String fieldName = params.peek();
      while ((fieldName = params.poll()) != null){
        pst.setString(idx++, fieldName);
      }
      pst.executeUpdate();
      try (ResultSet rs = pst.getGeneratedKeys()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      connection.rollback(savePoint);
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public long updateRecord(Connection connection, String sql, Queue<String> params) throws SQLException {
    Savepoint savePoint = connection.setSavepoint("savePointName");
    try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      int idx = 1;
      String fieldName = params.peek();
      while ((fieldName = params.poll()) != null){
        pst.setString(idx++, fieldName);
      }
      pst.executeUpdate();
      try (ResultSet rs = pst.getGeneratedKeys()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      connection.rollback(savePoint);
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public Optional<T> selectRecord(Connection connection, String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
    try (PreparedStatement pst = connection.prepareStatement(sql)) {
      pst.setLong(1, id);
      try (ResultSet rs = pst.executeQuery()) {
        return Optional.ofNullable(rsHandler.apply(rs));
      }
    }
  }

}
