package ru.otus.l06;

import ru.otus.l06.tester.TestErrorException;
import ru.otus.l06.tester.annotations.After;
import ru.otus.l06.tester.annotations.Before;
import ru.otus.l06.tester.annotations.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ForTesting {
    private BitShifterTests bitShifterTests;
    private final Logger logger = Logger.getLogger(ForTesting.class.getName());

    @Before
    public void before() {
        this.bitShifterTests = new BitShifterTests();
        logger.log(Level.INFO, TypeMess.Create.getMessage());
    }

    @Test
    public void testRightShift() {
        if (bitShifterTests == null) {
            logger.log(Level.WARNING, TypeMess.Error.getMessage());
        } else {
            int res1 = 8 >> 2; // for generate exception and testing Tester )
            int res2 = 8 >> 3;
            if (!(res1 == bitShifterTests.shiftRight(8) && res2 == bitShifterTests.shiftRight(8, 3))) {
                throw new TestErrorException("Test method shiftRight is incorrect");
            }
        }
    }

    @Test
    public void testLeftShift() {
        if (bitShifterTests == null) {
            logger.log(Level.WARNING, TypeMess.Error.getMessage());
        } else {
            int res1 = 8 << 1;
            int res2 = 8 << 3;
            if (!(res1 == bitShifterTests.shiftLeft(8) && res2 == bitShifterTests.shiftLeft(8, 3))) {
                throw new TestErrorException("Test method shiftLeft is incorrect");
            }
        }
    }

    @After
    public void after() {
        bitShifterTests = null;
        logger.log(Level.INFO, TypeMess.Destroy.getMessage());
    }

    private enum TypeMess{
        Create("BitShifter create"),
        Destroy("BitShifter destroyed"),
        Error("BitShifter error testing");

        private String message;

        TypeMess(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
