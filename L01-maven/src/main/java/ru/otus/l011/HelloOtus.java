package ru.otus.l011;
import com.google.common.base.CharMatcher;

import java.util.Arrays;

// to take from example: https://riptutorial.com/ru/guava/example/16431

/**
 * Created by tully.
 *
 * Example for L01.1
 *
 * To start the application:
 * mvn package
 * java -jar ./L01-maven/target/L01-maven-jar-with-dependencies.jar
 * java -cp "./L01-maven/target/L01-maven.jar:${HOME}/.m2/repository/com/google/guava/guava/27.1-jre/guava-27.1-jre.jar" ru.otus.l011.Main
 *
 * To unzip the jar:
 * unzip -l L01-maven.jar
 * unzip -l L01-maven-jar-with-dependencies.jar
 *
 * To build:
 * mvn package
 * mvn clean compile
 * mvn assembly:single
 * mvn clean compile assembly:single
 */

public class HelloOtus {
    private static final CharMatcher NON_ASCII = CharMatcher.ascii().negate();

    public static void main(String[] args) {

        String input = args[0];
        int nonAsciiCount = NON_ASCII.countIn(input);

        echo("Non-ASCII characters found: %d", nonAsciiCount);

        if (nonAsciiCount > 0) {
            int position = -1;
            char character = 0;

            while (position != NON_ASCII.lastIndexIn(input)) {
                position = NON_ASCII.indexIn(input, position + 1);
                character = input.charAt(position);

                echo("%s => \\u%04x", character, (int) character);
            }
        }
    }

    private static void echo(String s, Object... args) {
        System.out.println(String.format(s, args));
    }
}
