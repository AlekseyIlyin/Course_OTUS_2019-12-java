package ru.otus.l05;

import ru.otus.l05.annotation.ClassProxyInterface;
import ru.otus.l05.annotation.Log;

public class TestLogging implements ClassProxyInterface {
    @Log
    @Override
    public void calculation(int param) {

    }
}