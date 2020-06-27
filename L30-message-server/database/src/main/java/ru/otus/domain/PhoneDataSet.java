package ru.otus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@NoArgsConstructor
@Data
public class PhoneDataSet {
    private ObjectId id;
    private String number;

    public PhoneDataSet(String number) {
        this.number = number;
    }
}
