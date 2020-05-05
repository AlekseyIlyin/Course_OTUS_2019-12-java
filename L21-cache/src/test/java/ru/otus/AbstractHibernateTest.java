package ru.otus;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.hibernate.HibernateUtils;

public abstract class AbstractHibernateTest {
  private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";

  protected static final String FIELD_ID = "id";
  protected static final String FIELD_NAME = "name";

  protected static final String TEST_USER_NAME = "Вася";
  protected static final int TEST_USER_AGE = 18;
  protected static final String TEST_USER_ADDRESS = "Nikolaeva st.";
  protected static final String TEST_USER_PHONE = "+79156669999";

  protected static final String TEST_USER_NEW_NAME = "НЕ Вася";
  protected static final int TEST_USER_NEW_AGE = 10;
  protected static final String TEST_USER_NEW_ADDRESS = "Kiriva st.";
  protected static final String TEST_USER_NEW_PHONE = "+79157779999";

  protected static final String TEST_USER_NEW_NAME2 = "Совершенно точно НЕ Вася";
  protected static final int TEST_USER_AGE2 = 28;

  protected SessionFactory sessionFactory;

  @BeforeEach
  public void setUp() {
    sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, AddressDataSet.class, PhoneDataSet.class);
  }

  @AfterEach
  void tearDown() {
    sessionFactory.close();
  }

  protected User buildDefaultUser() {
    User user = new User(0, TEST_USER_NAME, TEST_USER_AGE, TEST_USER_ADDRESS, TEST_USER_PHONE);
    user.addPhone("+79207778833");
    return user;
  }

  protected void saveUser(User user) {
    try (Session session = sessionFactory.openSession()) {
      saveUser(session, user);
    }
  }

  protected void saveUser(Session session, User user) {
    session.beginTransaction();
    session.save(user);
    session.getTransaction().commit();
  }

  protected User loadUser(long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.find(User.class, id);
    }
  }

  protected EntityStatistics getUserStatistics() {
    Statistics stats = sessionFactory.getStatistics();
    return stats.getEntityStatistics(User.class.getName());
  }

}
