package ru.otus.l07;

import ru.otus.l07.ATM.ATM;
import ru.otus.l07.ATM.banknote.BanknoteRus;

public class Main {
    public static void main(String[] args) {
        ATM ATM = new ATM("ATM #1");
        ATM.addInStackBills(BanknoteRus.nom_500,10)
            .addInStackBills(BanknoteRus.nom_5000,10)
            .addInStackBills(BanknoteRus.nom_1000,10)
            .addInStackBills(BanknoteRus.nom_50, 10)
            .toAcceptMoney();
        ATM.printState();

        ATM.addInStackBills(BanknoteRus.nom_50, 50)
            .toAcceptMoney();
        ATM.printState();

        ATM.getMoney(7050)
            .toGiveOutMoney();
        ATM.printState();

        ATM.toAcceptMoney();
    }
}
