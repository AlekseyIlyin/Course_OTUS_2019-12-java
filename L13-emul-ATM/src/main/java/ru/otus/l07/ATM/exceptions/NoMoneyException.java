package ru.otus.l07.ATM.exceptions;

public class NoMoneyException extends RuntimeException {
    public NoMoneyException(String message) {
        super(message);
    }
}
