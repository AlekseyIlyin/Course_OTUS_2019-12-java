package ru.otus.l07.depatment.commands;

import ru.otus.l07.ATM.ATM;
import ru.otus.l07.depatment.Department;

public class PrintBalances implements Command {
    private final Department department;

    public PrintBalances(Department department) {
        this.department = department;
    }

    @Override
    public void execute() {
        System.out.println("Баланс по банкоматам:");
        department.getAtms().stream().map(atm -> atm.toString() + " - " + atm.getBalance()).forEach(System.out::println);
    }
}
