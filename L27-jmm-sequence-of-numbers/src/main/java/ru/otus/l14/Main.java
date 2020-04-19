package ru.otus.l14;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final int ITERATIONS = 2;
    private final int MAX_NUMBER = 9;
    private int currentIteration = 0;
    private volatile int currentStep = 0;
    private int vectorCount = 1;
    private List<CounterThread> threads = new ArrayList<>(2);

    public static void main(String[] args) {
        Main demo = new Main();
        demo.startCount();
    }

    public void startCount() {
        threads.add(new CounterThread(String.valueOf(threads.size()), 400));
        threads.add(new CounterThread(String.valueOf(threads.size()), 600));
        startAllThreads();
        while (currentIteration < ITERATIONS) {
            if (isPhaseSynchronized()) {
                changeCounter();
            } else {
                try {
                   Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        terminateAllThreads();
    }

    private void startAllThreads() {
        for (CounterThread thread : threads) {
            thread.start();
        }
    }

    private void terminateAllThreads() {
        for (CounterThread thread : threads) {
            thread.setTerminate();
        }
    }

    public void changeCounter() {
        currentStep = currentStep + vectorCount;

        if (currentStep == 0 && vectorCount == -1) {
            currentIteration++;
        }

        if (currentStep == MAX_NUMBER) {
            vectorCount = -1;
        } else if (currentStep == 0) {
            vectorCount = 1;
        }
    }

    private boolean isPhaseSynchronized() {
        boolean syncs = true;
        for (CounterThread thread : threads) {
            if (thread.getStepThread() != currentStep) {
                syncs = false;
            }
        }
        return syncs;
    }

    public class CounterThread extends Thread{
        private final String name;
        private final long wait_msc;
        private int stepThread = 0;
        private boolean terminate = false;

        public void setTerminate() {
            this.terminate = true;
        }

        public int getStepThread() {
            return stepThread;
        }

        @Override
        public String toString() {
            return name + ": " + stepThread;
        }

        @Override
        public void run() {
            while (!this.terminate) {
                if (stepThread != currentStep) {
                    stepThread = currentStep;
                    System.out.println(this);
               }
                try {
                    Thread.sleep(wait_msc);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public CounterThread(String name, int wait_msc) {
            this.name = "Thread_" + name;
            this.wait_msc = wait_msc;
        }
    }

}
