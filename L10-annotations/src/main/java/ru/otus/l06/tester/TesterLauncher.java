package ru.otus.l06.tester;

import ru.otus.l06.tester.annotations.After;
import ru.otus.l06.tester.annotations.Before;
import ru.otus.l06.tester.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class TesterLauncher {

    private final Set<Method> beforeEachMethods = new HashSet<>();
    private final Set<Method> testMethods = new HashSet<>();
    private final Set<Method> afterEachMethods = new HashSet<>();
    private Class clazz;
    private int failsCounter = 0;

    public TesterLauncher(Class className) {
        this.clazz = className;
    }

    public TestResult run() {
        System.out.println("Test launched from - " + clazz.getSimpleName());

        final var declaredMethods = clazz.getDeclaredMethods();
        for (Method method :
                declaredMethods) {
            for (Annotation annotation :
                    method.getDeclaredAnnotations()) {
                Class<? extends Annotation> aClass = annotation.annotationType();
                if (aClass == Before.class) {
                    beforeEachMethods.add(method);
                } else if (aClass == Test.class) {
                    testMethods.add(method);
                } else if (aClass == After.class) {
                    afterEachMethods.add(method);
                }
            }
        }

        if (!testMethods.isEmpty())
            runTests();
        else
            System.out.println("Class doesn't contains @Test methods!");

        return new TestResult(failsCounter, testMethods.size() - failsCounter, testMethods.size());
    }

    private void runTests() {
        for (Method method : testMethods) {
            try {
                final var constructor = clazz.getConstructor();
                final var newInstance = constructor.newInstance();

                if (!runBeforeEach(newInstance))
                    break;
                try {
                    System.out.println("Test for " + method.getName());
                    method.invoke(newInstance);
                } catch (InvocationTargetException error) {
                    error.getTargetException().printStackTrace();
                    failsCounter++;
                }

                for (Method afterEachMethods : afterEachMethods) {
                    afterEachMethods.invoke(newInstance);
                }
            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean runBeforeEach(Object newInstance) {
        try {
            for (Method beforeEachMethods : beforeEachMethods) {
                beforeEachMethods.invoke(newInstance);
            }
        } catch (InvocationTargetException exc) {
            System.err.println("Error when @BeforeEach:");
            exc.getTargetException().printStackTrace();
            failsCounter = testMethods.size();

            for (Method afterEachMethods : afterEachMethods) {
                try {
                    afterEachMethods.invoke(newInstance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.println("Error when @AfterEach:");
                    e.printStackTrace();
                }
            }
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("Error when @BeforeEach:");
            e.printStackTrace();
        }

        return true;
    }

    public class TestResult {
        private int failed;
        private int passed;
        private int tests;

        public TestResult(int failed, int passed, int tests) {
            this.failed = failed;
            this.passed = passed;
            this.tests = tests;
        }

        public void showResults() {
            System.out.println();
            System.out.println("*************Test results**************");
            System.out.println(String.format("*Tests failed: %d, passed: %d of %d tests*" ,failed, passed, tests));
            System.out.println("***************************************");
        }
    }

}
