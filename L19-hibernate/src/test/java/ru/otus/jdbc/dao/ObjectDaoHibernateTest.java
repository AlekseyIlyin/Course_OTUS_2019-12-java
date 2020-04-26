package ru.otus.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.AbstractHibernateTest;
import ru.otus.l10.core.model.PhoneDataSet;
import ru.otus.l10.core.model.User;
import ru.otus.l10.hibernate.dao.ObjectDaoHibernate;
import ru.otus.l10.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с пользователями должно ")
class ObjectDaoHibernateTest extends AbstractHibernateTest {

  private SessionManagerHibernate sessionManagerHibernate;
  private ObjectDaoHibernate userDaoHibernate;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
    userDaoHibernate = new ObjectDaoHibernate(sessionManagerHibernate);
  }

  @Test
  @DisplayName(" корректно загружать пользователя по заданному id")
  void shouldFindCorrectUserById() {

    User expectedUser = buildDefaultUser();
    saveUser(expectedUser);

    assertThat(expectedUser.getId()).isGreaterThan(0);

    Optional mayBeUser = userDaoHibernate.findById(expectedUser.getId(), User.class);

    System.out.println(expectedUser);
    System.out.println(mayBeUser.toString());

    assertThat(mayBeUser).isPresent().get().isEqualToComparingOnlyGivenFields(expectedUser,"id","name","age","address"); //  .isEqualToComparingFieldByField(expectedUser);
    assertThat(((User)mayBeUser.get()).getPhones()).containsExactlyInAnyOrderElementsOf(expectedUser.getPhones());
  }

  @DisplayName(" корректно сохранять пользователя")
  @Test
  void shouldCorrectSaveUser() {
    User expectedUser = buildDefaultUser();
    long id = userDaoHibernate.saveObject(expectedUser);

    assertThat(id).isGreaterThan(0);

    User actualUser = loadUser(id);
    assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

    expectedUser = new User(id, TEST_USER_NEW_NAME, TEST_USER_NEW_AGE, TEST_USER_NEW_ADDRESS, TEST_USER_NEW_PHONE);
    long newId = userDaoHibernate.saveObject(expectedUser);

    assertThat(newId).isGreaterThan(0).isEqualTo(id);
    actualUser = loadUser(newId);
    assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

  }

  @DisplayName(" возвращать менеджер сессий")
  @Test
  void getSessionManager() {
    assertThat(userDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
  }

}
