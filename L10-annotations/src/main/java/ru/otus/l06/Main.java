package ru.otus.l06;

import ru.otus.l06.tester.TesterLauncher;

public class Main {
    public static void main(String[] args) {
        TesterLauncher testerLauncher = new TesterLauncher(ForTesting.class);
        TesterLauncher.TestResult result = testerLauncher.run();
        result.showResults();
    }
}
