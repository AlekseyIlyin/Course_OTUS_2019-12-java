package ru.otus.model;

import lombok.*;

@NoArgsConstructor
@Data
public class AddressDataSet{
    //private long id;
    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressDataSet{");
        //sb.append("id=").append(id);
        sb.append("street='").append(street).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
