package ru.otus.l10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l10.core.dao.UserDaoTemplate;
import ru.otus.l10.core.model.User;
import ru.otus.l10.core.service.DBService;
import ru.otus.l10.core.service.DbServiceImpl;
import ru.otus.l10.h2.DataSourceH2;
import ru.otus.l10.jdbc.DbExecutor;
import ru.otus.l10.jdbc.dao.ObjectDaoJdbc;
import ru.otus.l10.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
  private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

  public static void main(String[] args) throws Exception {
    DataSource dataSource = new DataSourceH2();
    DbServiceDemo demo = new DbServiceDemo();

    demo.createTable(dataSource);

    SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutor<User> dbExecutor = new DbExecutor<>();
    UserDaoTemplate objectDao = new ObjectDaoJdbc(sessionManager, dbExecutor, User.class);

    DBService dbServiceObject = new DbServiceImpl(objectDao);

    Long userId = 0L;
    User user = new User(userId, "dbServiceUser", 16);
    dbServiceObject.saveObject(user);
    logger.info("Object create and save: " + user);

    user.setAge(18);
    dbServiceObject.updateObject(user); // Помогите понять плиз, почему запись в БД не обновляется?
    logger.info("Object modified: " + user);

    var userGetFromDB = dbServiceObject.getUser(userId, objectDao.getClass());
    System.out.println(userGetFromDB);
  }

  private void createTable(DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(255), age int(3))")) {
      pst.executeUpdate();
    }
    System.out.println("table created");
  }
}
