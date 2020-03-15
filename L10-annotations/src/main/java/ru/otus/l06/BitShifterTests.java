package ru.otus.l06;

public class BitShifterTests {
    public int shiftRight(int x) {
        return x >> 1;
    }

    public int shiftRight(int x, int signs) {
        return x >> signs;
    }

    public int shiftLeft(int x) {
        return x << 1;
    }

    public int shiftLeft(int x, int signs) {
        return x << signs;
    }

}
