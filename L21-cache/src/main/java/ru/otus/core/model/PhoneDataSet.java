package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phones")
public class PhoneDataSet implements ModelDb{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Column(name = "number")
    private String number;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PhoneDataSet{");
        sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Override
    public long getId() {
        return id;
    }
}
