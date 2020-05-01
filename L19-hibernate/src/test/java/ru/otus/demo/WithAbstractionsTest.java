package ru.otus.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.AbstractHibernateTest;
import ru.otus.l10.core.dao.DaoTemplate;

import ru.otus.l10.core.model.User;
import ru.otus.l10.core.service.DBService;
import ru.otus.l10.core.service.DbServiceImpl;
import ru.otus.l10.hibernate.dao.ObjectDaoHibernate;
import ru.otus.l10.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
public class WithAbstractionsTest extends AbstractHibernateTest {

  private DBService<User> dbService;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    DaoTemplate userDao = new ObjectDaoHibernate(sessionManager);
    dbService = new DbServiceImpl(userDao);
  }

  @Test
  @DisplayName(" корректно сохранять пользователя")
  void shouldCorrectSaveUser(){
    User savedUser = buildDefaultUser();

    long id = dbService.saveObject(savedUser);
    User loadedUser = loadUser(id);

    //assertThat(loadedUser).isNotNull().isEqualToComparingFieldByField(savedUser);
    assertThat(loadedUser).isEqualToComparingOnlyGivenFields(savedUser, "id","name","age","address"); //.isEqualToComparingFieldByField(savedUser);
    assertThat(loadedUser.getPhones()).containsExactlyInAnyOrderElementsOf(savedUser.getPhones());

    System.out.println(savedUser);
    System.out.println(loadedUser);
  }

  @Test
  @DisplayName(" корректно загружать пользователя")
  void shouldLoadCorrectUser(){
    User savedUser = buildDefaultUser();
    saveUser(savedUser);

    Optional<User> mayBeUser = dbService.getObject(savedUser.getId(), User.class);

    assertThat(mayBeUser).isPresent().get().isEqualToComparingOnlyGivenFields(savedUser, "id","name","age","address"); //.isEqualToComparingFieldByField(savedUser);
    assertThat(mayBeUser.get().getPhones()).containsExactlyInAnyOrderElementsOf(savedUser.getPhones());

    System.out.println(savedUser);
    mayBeUser.ifPresent(System.out::println);
  }

  @Test
  @DisplayName(" корректно изменять ранее сохраненного пользователя")
  void shouldCorrectUpdateSavedUser(){
    User savedUser = buildDefaultUser();
    saveUser(savedUser);

    User savedUser2 = new User(savedUser.getId(), TEST_USER_NEW_NAME, TEST_USER_NEW_AGE, TEST_USER_NEW_ADDRESS);
    long id = dbService.saveObject(savedUser2);
    User loadedUser = loadUser(id);

    assertThat(loadedUser).isNotNull().isEqualToComparingOnlyGivenFields(savedUser2, "id","name","age","address"); //.isEqualToComparingFieldByField(savedUser);

    System.out.println(savedUser);
    System.out.println(savedUser2);
    System.out.println(loadedUser);
  }

}
