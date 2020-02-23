package ru.otus.l07.ATM.exceptions;

public class NoMoneyException extends Exception {
    public NoMoneyException(String message) {
        super(message);
    }
}
