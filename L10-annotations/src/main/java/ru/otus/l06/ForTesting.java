package ru.otus.l06;

import ru.otus.l06.tester.TestErrorExtension;
import ru.otus.l06.tester.annotations.After;
import ru.otus.l06.tester.annotations.Before;
import ru.otus.l06.tester.annotations.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ForTesting {
    private BitShifter bitShifter;
    private final Logger logger = Logger.getLogger(ForTesting.class.getName());

    @Before
    public void before() {
        this.bitShifter = new BitShifter();
        logger.log(Level.INFO, TypeMess.Create.getMessage());
    }

    @Test
    public void testRightShift() {
        if (bitShifter == null) {
            logger.log(Level.WARNING, TypeMess.Error.getMessage());
        } else {
            int res1 = 8 >> 2; // for generate exception and testing Tester )
            int res2 = 8 >> 3;
            if (!(res1 == bitShifter.shiftRight(8) && res2 == bitShifter.shiftRight(8, 3))) {
                throw new TestErrorExtension("Test method shiftRight is incorrect");
            }
        }
    }

    @Test
    public void testLeftShift() {
        if (bitShifter == null) {
            logger.log(Level.WARNING, TypeMess.Error.getMessage());
        } else {
            int res1 = 8 << 1;
            int res2 = 8 << 3;
            if (!(res1 == bitShifter.shiftLeft(8) && res2 == bitShifter.shiftLeft(8, 3))) {
                throw new TestErrorExtension("Test method shiftLeft is incorrect");
            }
        }
    }

    @After
    public void after() {
        bitShifter = null;
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
