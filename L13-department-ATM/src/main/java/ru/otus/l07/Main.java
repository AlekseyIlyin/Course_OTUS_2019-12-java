package ru.otus.l07;

import ru.otus.l07.ATM.ATM;
import ru.otus.l07.ATM.banknote.BanknoteRus;
import ru.otus.l07.depatment.Department;
import ru.otus.l07.depatment.commands.*;

public class Main {
    public static void main(String[] args) {
        Department department = new Department();
        Executor executor = new Executor();

        ATM atm1 = new ATM("ATM #1");
        atm1.addInStackBills(BanknoteRus.nom_500,10)
            .addInStackBills(BanknoteRus.nom_5000,10)
            .addInStackBills(BanknoteRus.nom_1000,10)
            .addInStackBills(BanknoteRus.nom_50, 10)
            .toAcceptMoney();

        ATM atm2 = new ATM("ATM #2");
        atm2.addInStackBills(BanknoteRus.nom_500,1)
                .addInStackBills(BanknoteRus.nom_50, 1)
                .toAcceptMoney();

        ATM atm3 = new ATM("ATM #3");
        atm3.addInStackBills(BanknoteRus.nom_500,10)
                .addInStackBills(BanknoteRus.nom_5000,5)
                .addInStackBills(BanknoteRus.nom_50, 1)
                .toAcceptMoney();

        System.out.println("Инициализация департамента");
        executor
                .addCommand(new AddAtm(department, atm1))
                .addCommand(new AddAtm(department, atm2))
                .addCommand(new AddAtm(department, atm3))
                .addCommand(new PrintBalances(department))
                .executeCommands();

        executor
                .addCommand(new SaveState(department))
                .addCommand(new PrintBalances(department))
                .executeCommands();

        System.out.println("\nИзменяем состояние банкоматов");
        atm1.getMoney(50).toGiveOutMoney();
        atm2.getMoney(50).toGiveOutMoney();
        atm3.getMoney(50).toGiveOutMoney();

        executor
                .addCommand(new PrintBalances(department))
                .executeCommands();

        System.out.println("\nВосстанавливаем состояние");
        executor
                .addCommand(new RestoreState(department))
                .addCommand(new PrintBalances(department))
                .executeCommands();
    }
}
