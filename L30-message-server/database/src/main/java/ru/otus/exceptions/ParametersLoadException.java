package ru.otus.exceptions;

public class ParametersLoadException extends RuntimeException{
    public ParametersLoadException(String message) {
        super("Error load parameters: " + message);
    }
}
