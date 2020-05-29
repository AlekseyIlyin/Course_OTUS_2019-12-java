package ru.otus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddressDataSet{
    private String id;
    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }
}
