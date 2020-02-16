package ru.otus.l03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
1) Проверяйте на коллекциях с 20 и больше элементами.
        2) DIYarrayList должен имплементировать ТОЛЬКО ОДИН интерфейс - List.
        3) Если метод не имплементирован, то он должен выбрасывать исключение UnsupportedOperationException.

Проверить, что на ней работают методы из java.util.Collections:
        Collections.addAll(Collection<? super T> c, T... elements)
        Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
        Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
*/

public class Main {
    public static void main(String[] args) {
        final int SIZE_COLLECTION_FOR_TEST = 20; // кратно 5
        final int NUM_TESTS = 30;

        int sumTimeTests = 0;

        final Integer[] integers = new Integer[SIZE_COLLECTION_FOR_TEST];

        for (int i = 0; i < 10; i++) {

            long startMs = new Date().getTime();

            // + test for addAll
            System.out.println("AddAll in DIY list:");
            DIYarrayList<Integer> integerDIYarrayList = new DIYarrayList<>();
            fillIntegerRandomValues(integers);
            System.out.println(Arrays.toString(integers));
            Collections.addAll(integerDIYarrayList, integers);
            System.out.println(integerDIYarrayList.toString());
            // - test for addAll

            // + test for copy
            //Collections.copy();  static <T> void copy(List<? super T> dest, List<? extends T> src)
            System.out.println("\nCopy in DIY list:");
            fillIntegerRandomValues(integers);
            System.out.println(Arrays.toString(integers));
            integerDIYarrayList = new DIYarrayList<>(SIZE_COLLECTION_FOR_TEST);
            ArrayList<Integer> integerArrayList = new ArrayList<>(SIZE_COLLECTION_FOR_TEST);
            Collections.addAll(integerArrayList, integers);
            Collections.copy(integerDIYarrayList, integerArrayList);
            System.out.println(integerDIYarrayList.toString());
            // - test for copy

            // + test for sort
            //Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
            System.out.println("\nSorted DIY list:");
            System.out.println(integerArrayList);
            Collections.sort(integerDIYarrayList);
            System.out.println(integerDIYarrayList.toString());
            // - test for sort

            sumTimeTests += (int) (new Date().getTime() - startMs);
        }

        System.out.println(String.format("Elapsed average time %d ms, use %d iteration", sumTimeTests / NUM_TESTS, NUM_TESTS));
    }

    private static void fillIntegerRandomValues(Integer[] randomIntegerArray) {
        for (int index = 0; index < randomIntegerArray.length; index++) {
            randomIntegerArray[index] = ((int) (Math.random() * 100));
        }
    }
}
