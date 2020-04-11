package ru.otus.l10.core.model;

import ru.otus.l10.core.annotations.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {
  @Id
  private final long id;
  private final String name;
  private int age;

  public void setAge(int age) {
    this.age = age;
  }

  public int getAge() {
    return age;
  }

  public User() {
    this.id = 0L;
    this.name = "";
    this.age = 0;
  }

  public User(long id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", age=").append(age);
    sb.append('}');
    return sb.toString();
  }
}
