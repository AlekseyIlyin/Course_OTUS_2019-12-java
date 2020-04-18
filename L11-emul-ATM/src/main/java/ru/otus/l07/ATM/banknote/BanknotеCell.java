package ru.otus.l07.ATM.banknote;

import ru.otus.l07.ATM.exceptions.NoMoreBanknoteException;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

public interface BanknotеCell {
    int getNumBanknotes();
    void putBanknote(int num) throws NoPlaceBanknoteException;
    int getBanknote(int num) throws NoMoreBanknoteException;
    int getSum();
    boolean isEmpty();
    BanknoteRus getTypeBanknote();
}
