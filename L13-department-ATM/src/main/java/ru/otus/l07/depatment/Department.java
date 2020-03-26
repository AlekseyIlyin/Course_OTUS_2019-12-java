package ru.otus.l07.depatment;

import ru.otus.l07.ATM.ATM;

import java.util.HashSet;
import java.util.Set;

public class Department {
    private Set<ATM> atms = new HashSet<>();

    public Set<ATM> getAtms() {
        return atms;
    }
}
