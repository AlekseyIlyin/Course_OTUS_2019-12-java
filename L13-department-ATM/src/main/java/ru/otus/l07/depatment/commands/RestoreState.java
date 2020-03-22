package ru.otus.l07.depatment.commands;

import ru.otus.l07.ATM.ATM;
import ru.otus.l07.depatment.Department;

public class RestoreState implements Command {
    private final Department department;

    public RestoreState(Department department) {
        this.department = department;
    }

    @Override
    public void execute() {
        department.getAtms().forEach(ATM::restoreState);
    }
}
