package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Кэш должен ")
class MyCacheTest {
    static private final String TEST_KEY = "test_key";
    static private final Integer TEST_VALUE = 123;
    static private final HwCache<String, Integer> cache = new MyCache<>();

    @DisplayName(" корректно добавляться значения")
    @Test
    void put() {
        cache.put(TEST_KEY, TEST_VALUE);
        assertTrue(cache.get(TEST_KEY) == TEST_VALUE);
        cache.remove(TEST_KEY);
    }

    @DisplayName(" корректно удаляться значения")
    @Test
    void remove() {
        cache.put(TEST_KEY, TEST_VALUE);
        assertTrue(cache.get(TEST_KEY) == TEST_VALUE);
        cache.remove(TEST_KEY);
        assertNull(cache.get(TEST_KEY));
    }

    @DisplayName(" корректно извлекаться значения по ключу (идентификатору)")
    @Test
    void get() {
        cache.put(TEST_KEY, TEST_VALUE);
        assertTrue(cache.get(TEST_KEY) == TEST_VALUE);
    }

}