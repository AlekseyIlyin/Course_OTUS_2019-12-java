package ru.otus.l07;

import ru.otus.l07.ATM.ATM;
import ru.otus.l07.ATM.banknote.BanknoteRus;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM("ATM #1");
        atm.addInStackBills(BanknoteRus.nom_500,10)
            .addInStackBills(BanknoteRus.nom_5000,10)
            .addInStackBills(BanknoteRus.nom_1000,10)
            .addInStackBills(BanknoteRus.nom_50, 10)
            .toAcceptMoney();
        atm.printState();

        atm.addInStackBills(BanknoteRus.nom_50, 50)
            .toAcceptMoney();
        atm.printState();

        atm.getMoney(7050)
            .toGiveOutMoney();
        atm.printState();

        atm.toAcceptMoney();
    }
}
