package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class AddressDataSet implements ModelDb{

    public AddressDataSet(String street) {
        this.street = street;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressDataSet{");
        sb.append("id=").append(id);
        sb.append(", street='").append(street).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDataSet)) return false;

        AddressDataSet that = (AddressDataSet) o;

        return street.equals(that.street);
    }

    @Override
    public int hashCode() {
        return street.hashCode();
    }
}
