package ru.otus.l07.ATM;

public interface State {
    void saveState();
    void restoreState();
}
