package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.User;
import ru.otus.hibernate.dao.ObjectDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao с Кэшем должно ")
class ObjectDaoHibernateWithCacheTest extends AbstractHibernateTest {

  private SessionManagerHibernate sessionManagerHibernate;
  private ObjectDaoHibernate<User> userDaoHibernate;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
    userDaoHibernate = new ObjectDaoHibernate(sessionManagerHibernate, true);
  }

  @DisplayName(" при использовании Кэша возвращать данные быстрее")
  @Test
  void сompareOfDataAcquisitionSpeed() {
    User expectedUser = buildDefaultUser();
    saveUser(expectedUser);

    assertThat(expectedUser.getId()).isGreaterThan(0);

    long time1 = System.currentTimeMillis();
    Optional mayBeUser = userDaoHibernate.findById(expectedUser.getId(), User.class);
    Long timeOffCache = System.currentTimeMillis() - time1;
    long time2 = System.currentTimeMillis();
    mayBeUser = userDaoHibernate.findById(expectedUser.getId(), User.class);
    Long timeOnCache = System.currentTimeMillis() - time2;

    System.out.println("Созданный пользователь:");
    System.out.println(expectedUser);
    System.out.println("\nПолученный пользователь с использованием Кэша");
    System.out.println(mayBeUser.toString());

    System.out.println(String.format("\nЗатраченное время - без кэша: %d   с кэшем: %d", timeOffCache, timeOnCache));

    assertThat(mayBeUser).isPresent().get().isEqualToComparingOnlyGivenFields(expectedUser,"id","name","age","address"); //  .isEqualToComparingFieldByField(expectedUser);
    assertThat(((User)mayBeUser.get()).getPhones()).containsExactlyInAnyOrderElementsOf(expectedUser.getPhones());
    assertThat(timeOffCache).isGreaterThan(timeOnCache);
  }

}
