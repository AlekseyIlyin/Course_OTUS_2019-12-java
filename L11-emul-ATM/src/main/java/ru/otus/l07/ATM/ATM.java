package ru.otus.l07.ATM;

import ru.otus.l07.ATM.banknote.BanknoteRus;
import ru.otus.l07.ATM.banknote.MoneyBox;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

public class ATM {
    private String name;
    private MoneyBox cassette;
    private MoneyBox stackBills;

    @Override
    public String toString() {
        return name;
    }

    public ATM(String name) {
        this.name = name;
        initialize();
    }

    public void initialize() {
        this.cassette = new MoneyCassette();
        this.stackBills = new MoneyStackBills();
    }

    public ATM addInStackBills(BanknoteRus banknote, int quantity) {
        System.out.println("Положили в лоток: " + banknote + " количество: " + quantity);
        try {
            stackBills.putBanknote(banknote, quantity);
        } catch (NoPlaceBanknoteException e) {
            System.out.println("Лоток переполнен! Максимальное кол-во купюр: " + MoneyStackBills.SIZE_IN_OUT_TRAY);
            //e.printStackTrace();
        }
        return this;
    }

    public void toAcceptMoney() {
        System.out.println(stackBills.getState() + "\nПринимаем купюры в кассету");
        cassette.loadFromOverMoneyBox(stackBills);
        stackBills.initialize();
    }

    public void toGiveOutMoney() {
        System.out.println("Выдача купюр из лотка...");
        System.out.println(stackBills.getState());
        stackBills.initialize();
    }

    public ATM getMoney(int sum) {
        System.out.println("Получаем сумму в лоток из кассеты: " + sum);
        stackBills = cassette.getBanknotesBySum(sum);
        return this;
    }

    public int getBalance() {
        return cassette.getBalance();
    }

    public void printState() {
        System.out.println();
        StringBuilder stringBuilder = new StringBuilder(toString())
            .append('\n')
            .append("Сумма: ").append(getBalance()).append('\n')
            .append(cassette.getState()).append('\n')
            .append(stackBills.getState()).append('\n');
        System.out.println(stringBuilder.toString());
    }
}
