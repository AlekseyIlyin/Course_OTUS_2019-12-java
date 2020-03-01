package ru.otus.l05;

import ru.otus.l05.annotation.IoC;

public class Demo {
    public static void main(String[] args) {
        var testLogging = IoC.createLoggerForClass();
        testLogging.calculation(6);
        testLogging.calculation(6,7);
        testLogging.calculation(6,7, 8);
    }
}
