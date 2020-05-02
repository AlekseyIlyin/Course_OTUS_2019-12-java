package ru.otus.core.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements ModelDb {

  public User() {
    phones = new ArrayList<>();
  }

  public User(long id, String name, int age, String address, String mainPhone) {
    this.id = id;
    this.name = name;
    this.age = age;
    setAddress(new AddressDataSet(address));
    addPhone(mainPhone);
  }

  public User(long id, String name, int age, String address) {
    this.id = id;
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

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "age")
  private int age;

  @OneToOne(targetEntity = AddressDataSet.class)
  @Cascade({org.hibernate.annotations.CascadeType.ALL})
  @JoinColumn(name = "addresses_id")
  private AddressDataSet address;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  @Cascade({org.hibernate.annotations.CascadeType.ALL})
  private List<PhoneDataSet> phones = new ArrayList<>();

  public void addPhone(String phoneNumber) {
    phones.add(new PhoneDataSet(0,phoneNumber,this));
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", age=").append(age);
    sb.append(", address=").append(address);
    sb.append(", phones=").append(phones);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;

    User user = (User) o;

    if (id != user.id) return false;
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

  @Override
  public int hashCode() {
    return Objects.hash(id, name, address, phones);
  }
}
