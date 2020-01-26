Домашнее задание<p>
<H5>DIY ArrayList
Написать свою реализацию ArrayList на основе массива.
class DIYarrayList<T> implements List<T>{...}</H5>

Проверить, что на ней работают методы из java.util.Collections:
Collections.addAll(Collection<? super T> c, T... elements)
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)

<ol>
<li>Проверяйте на коллекциях с 20 и больше элементами.</li>
<li>DIYarrayList должен имплементировать ТОЛЬКО ОДИН интерфейс - List.</li>
<li>Если метод не имплементирован, то он должен выбрасывать исключение UnsupportedOperationException.</li>
</ol>