package ru.otus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "name")
  private String name;
  @Column(name = "login")
  private String login;
  @Column(name = "password")
  private String password;

  @Column(name = "age")
  private int age;

  @OneToOne(cascade = CascadeType.ALL, targetEntity = AddressDataSet.class)
  @JoinColumn(name = "addresses_id")
  private AddressDataSet address;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
  private List<PhoneDataSet> phones = new ArrayList<>();

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

}
