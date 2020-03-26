package ru.otus.l07.depatment.commands;

import ru.otus.l07.ATM.ATM;
import ru.otus.l07.depatment.Department;

public class AddAtm implements Command {
    private final Department department;
    private final ATM atm;

    public AddAtm(Department department, ATM atm) {
        this.department = department;
        this.atm = atm;
    }

    @Override
    public void execute() {
        department.getAtms().add(atm);
    }
}
