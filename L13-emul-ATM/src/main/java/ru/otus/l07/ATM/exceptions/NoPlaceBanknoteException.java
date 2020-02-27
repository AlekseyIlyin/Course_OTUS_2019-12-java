package ru.otus.l07.ATM.exceptions;

public class NoPlaceBanknoteException extends RuntimeException {
    public NoPlaceBanknoteException(String NameCell) {
        super("Нет места в ячейке: " + NameCell);
    }
}
