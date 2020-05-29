package ru.otus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PhoneDataSet {
    private String id;
    private String number;

    public PhoneDataSet(String number) {
        this.number = number;
    }
}
