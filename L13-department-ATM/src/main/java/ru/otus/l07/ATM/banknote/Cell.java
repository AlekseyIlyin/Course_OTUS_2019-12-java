package ru.otus.l07.ATM.banknote;

import ru.otus.l07.ATM.MoneyCassette;
import ru.otus.l07.ATM.exceptions.NoMoreBanknoteException;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

import java.util.Objects;

public abstract class Cell implements BanknotеCell{

    protected int numBanknotes = 0;
    protected final BanknoteRus typeBanknote;

    @Override
    public boolean isEmpty() {
        return numBanknotes == 0;
    }

    @Override
    public abstract void putBanknote(int num) throws NoPlaceBanknoteException;

    protected Cell(BanknoteRus typeBanknote) {
        this.typeBanknote = typeBanknote;
    }

    protected Cell(BanknoteRus typeBanknote, int numBanknotes) {
        this.typeBanknote = typeBanknote;
        this.numBanknotes = numBanknotes;
    }

    @Override
    public int getNumBanknotes() {
        return numBanknotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return numBanknotes == cell.numBanknotes &&
                typeBanknote == cell.typeBanknote;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numBanknotes, typeBanknote);
    }

    @Override
    public int getBanknote(int num) throws NoMoreBanknoteException {
        if (num > numBanknotes) {
            throw new NoMoreBanknoteException(toString());
        }
        numBanknotes -= num;
        return num;
    }

    @Override
    public int getSum() {
        return typeBanknote.getCost() * numBanknotes;
    }

    @Override
    public String toString() {
        return new StringBuilder("Ячейка: ").append(typeBanknote).append(" количество: ").append(numBanknotes).append(" на сумму: ").append(getSum()).toString();
    }

    @Override
    public BanknoteRus getTypeBanknote() {
        return this.typeBanknote;
    }
}
