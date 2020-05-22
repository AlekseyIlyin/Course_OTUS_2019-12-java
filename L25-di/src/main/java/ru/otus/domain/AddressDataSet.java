package ru.otus.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
public class AddressDataSet{
    public AddressDataSet(String street) {
        this.street = street;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

}
