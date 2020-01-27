package main.java.ru.otus.l03;

import java.util.ArrayList;
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
        final int SIZE_COLLECTION_FOR_TEST = 10000; // кратно 5
        final int NUM_TESTS = 30;

        int sumTimeTests = 0;

        for (int i = 0; i < 10; i++) {

            long startMs = new Date().getTime();

            // + test for addAll
            System.out.println("AddAll in DIY list:");
            DIYarrayList<Integer> integerDIYarrayList = new DIYarrayList<>();

            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int index = 0; index < SIZE_COLLECTION_FOR_TEST; index++) {
                arrayList.add((int) (Math.random() * 100));
            }

            System.out.println(arrayList.toString());

            for (int indexForFive = 0; indexForFive < SIZE_COLLECTION_FOR_TEST; indexForFive++) {
                //Collections.addAll(Collection<? super T> c, T... elements)
                Collections.addAll(integerDIYarrayList, arrayList.get(indexForFive));
            }
            System.out.println(integerDIYarrayList.toString());
            // - test for addAll

            // + test for copy
            //Collections.copy();  static <T> void copy(List<? super T> dest, List<? extends T> src)
            System.out.println("\nCopy in DIY list:");
            for (int index = 0; index < SIZE_COLLECTION_FOR_TEST; index++) {
                arrayList.set(index, ((int) (Math.random() * 100)));
            }
            System.out.println(arrayList.toString());
            integerDIYarrayList = new DIYarrayList<>(SIZE_COLLECTION_FOR_TEST);
            Collections.copy(integerDIYarrayList, arrayList);
            System.out.println(integerDIYarrayList.toString());
            // - test for copy

            // + test for sort
            //Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
            System.out.println("\nSorted DIY list:");
            System.out.println(arrayList);
            Collections.sort(integerDIYarrayList);
            System.out.println(integerDIYarrayList.toString());
            // - test for sort

            sumTimeTests += (int) (new Date().getTime() - startMs);
        }

        System.out.println(String.format("Elapsed average time %d ms, use %d iteration", sumTimeTests / NUM_TESTS, NUM_TESTS));
    }
}
