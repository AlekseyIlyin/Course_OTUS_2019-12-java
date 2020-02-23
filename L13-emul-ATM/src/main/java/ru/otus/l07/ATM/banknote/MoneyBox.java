package ru.otus.l07.ATM.banknote;

import ru.otus.l07.ATM.MoneyStackBills;
import ru.otus.l07.ATM.exceptions.NoMoneyException;
import ru.otus.l07.ATM.exceptions.NoMoreBanknoteException;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

import java.util.Map;
import java.util.TreeMap;

public abstract class MoneyBox {
    protected String name;

    protected final TreeMap<BanknoteRus,BanknotеCell> box = new TreeMap<>();

    public abstract void initialize();

    public abstract BanknotеCell getCell (BanknoteRus banknote);

    public void putBanknote(BanknoteRus banknote, int quantity) throws NoPlaceBanknoteException {
        getCell(banknote).putBanknote(quantity);
    }

    public Map<BanknoteRus, BanknotеCell> getCells() {
        return box;
    }

    public void loadFromOverMoneyBox (MoneyBox overBox) throws NoMoreBanknoteException, NoPlaceBanknoteException {
        for (BanknotеCell cellOverBox : overBox.getCells().values()) {
            putBanknote(cellOverBox.getTypeBanknote(),cellOverBox.getBanknote(cellOverBox.getNumBanknotes()));
        }
    }

    public boolean isEmpty() {
        return getBalance() == 0;
    }

    public String getState() {
        StringBuilder stringBuilder = new StringBuilder(name);
        if (isEmpty()) {
            stringBuilder.append(" ПУСТО!");
        } else {
            stringBuilder.append("  сумма: ").append(getBalance()).append('\n');
            for (BanknotеCell cell : box.values()) {
                if (!cell.isEmpty()) {
                    stringBuilder.append(cell.getTypeBanknote()).append(" - ").append(cell.getNumBanknotes()).append(" на сумму: ").append(cell.getSum()).append('\n');
                }
            }
        }
        return stringBuilder.toString();
    }

    public int getBalance() {
        return box.values().stream()
                .mapToInt(cell -> cell.getSum())
                .sum();
    }

    public MoneyBox getBanknotesBySum(int sum) throws NoMoneyException, NoPlaceBanknoteException, NoMoreBanknoteException {
        MoneyBox moneyBox = new MoneyStackBills();
        if (sum > getBalance()) {
            throw new NoMoneyException(String.format("Сумму %d невозможно выдать! Максимальная для выдачи сумма: %d", sum, getBalance()));
        } else {
            int sumForDispense = sum;
            for (BanknotеCell banknotеCell : box.values()) {
                if (sumForDispense == 0) {
                    break;
                } else if (banknotеCell.getNumBanknotes() > 0) {
                    int costBanknote = banknotеCell.getTypeBanknote().getCost();
                    int calcNumsBanknotes = Math.min(sumForDispense / costBanknote, banknotеCell.getNumBanknotes());
                    if (calcNumsBanknotes > 0) {
                        moneyBox.putBanknote(banknotеCell.getTypeBanknote(),banknotеCell.getBanknote(calcNumsBanknotes));
                        sumForDispense -= calcNumsBanknotes * costBanknote;
                    }
                }
            }
        }
        return moneyBox;
    }
}
