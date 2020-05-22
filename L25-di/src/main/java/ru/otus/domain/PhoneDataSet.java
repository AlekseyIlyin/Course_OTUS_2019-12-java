package ru.otus.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phones")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    private final String number;
}
