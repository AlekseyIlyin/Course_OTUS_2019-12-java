package ru.otus.l06.tester;

public class TestErrorExtension extends RuntimeException {
    public TestErrorExtension(String message) {
        super(message);
    }
}
