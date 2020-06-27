package ru.otus.domain;

import lombok.Data;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private ObjectId id;
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

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

}
