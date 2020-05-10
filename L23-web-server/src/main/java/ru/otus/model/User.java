package ru.otus.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
  private ObjectId _id;
  private String name;

  private String login;
  private String password;

  private int age;
  private AddressDataSet address;
  private List<PhoneDataSet> phones = new ArrayList<>();

  public User() {
    phones = new ArrayList<>();
  }

  public User(String name, int age, String address) {
    this.name = name;
    this.age = age;
    setAddress(new AddressDataSet(address));
  }

  public User(String name, int age, String address, String mainPhone) {
    this.name = name;
    this.age = age;
    setAddress(new AddressDataSet(address));
    addPhone(mainPhone);
  }

  public User(String name, String login, String password) {
    phones = new ArrayList<>();
    this.name = name;
    this.login = login;
    this.password = password;
  }

  public void addPhone(String phoneNumber) {
    phones.add(new PhoneDataSet(phoneNumber));
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(_id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", age=").append(age);
    sb.append(", address=").append(address);
    sb.append(", phones=").append(phones);
    sb.append('}');
    return sb.toString();
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;

    User user = (User) o;

    if (!_id.equals(user._id)) return false;
    if (age != user.age) return false;
    if (!name.equals(user.name)) return false;
    if (address != null ? !address.equals(user.address) : user.address != null) return false;

    List<PhoneDataSet> overUserPhones = user.getPhones();
    if (phones.size() != overUserPhones.size()) {
      return false;
    }
    for (PhoneDataSet phone : phones) {
      if (!overUserPhones.contains(phone)) {
        return false;
      }
    }
    return true;
  }

}
