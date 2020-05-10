package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneDataSet {
    //private long id;
    private String number;
    //private User user;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PhoneDataSet{");
        //sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
        sb.append('}');
        return sb.toString();
    }

/*
    @Override
    public long getId() {
        return id;
    }
*/
}
