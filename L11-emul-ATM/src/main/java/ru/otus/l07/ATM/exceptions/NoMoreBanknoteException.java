package ru.otus.l07.ATM.exceptions;

public class NoMoreBanknoteException extends RuntimeException {
    public NoMoreBanknoteException(String NameCell) {
        super("Нет банкнот в ячейке: " + NameCell);
    }
}
