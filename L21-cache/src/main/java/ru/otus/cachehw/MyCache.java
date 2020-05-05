package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
  //Надо реализовать эти методы
  private final List<HwListener<K,V>> listeners = new ArrayList<>();
  private final Map<K, V> cache = new WeakHashMap<>();

  @Override
  public void put(K key, V value) {
    cache.put(key, value);
    listeners.forEach(pair -> pair.notify(key,value,"put"));
  }

  @Override
  public void remove(K key) {
    var delCacheValue = cache.remove(key);
    listeners.forEach(pair -> pair.notify(key,delCacheValue,"del"));
  }

  @Override
  public V get(K key) {
    return cache.get(key);
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    listeners.remove(listener);
  }
}
