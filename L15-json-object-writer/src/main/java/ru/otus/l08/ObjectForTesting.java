package ru.otus.l08;

import java.util.*;

public class ObjectForTesting {
  private final int value1;
  private final String value2;
  private final int value3;
  private final int[] array = new int[3];
  private final Set<String> stringSet = new HashSet<>();
  private final Map<String,Integer> hashMap = new HashMap<>();

  ObjectForTesting(int value1, String value2, int value3) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    for (int i = 0; i < 3; i++) {
      array[i] = (int) (Math.random() * 100.0);
      stringSet.add("Set_" + i);
      hashMap.put("Set_" + i, i);
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BagOfPrimitives{");
    sb.append("value1=").append(value1);
    sb.append(", value2='").append(value2).append('\'');
    sb.append(", value3=").append(value3);
    sb.append(", array=").append(Arrays.toString(array));
    sb.append(", stringSet=").append(stringSet);
    sb.append(", hashMap=").append(hashMap);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ObjectForTesting that = (ObjectForTesting) o;
    return value1 == that.value1 &&
            value3 == that.value3 &&
            Objects.equals(value2, that.value2) &&
            Arrays.equals(array, that.array) &&
            Objects.equals(stringSet, that.stringSet) &&
            Objects.equals(hashMap, that.hashMap);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(value1, value2, value3, stringSet, hashMap);
    result = 31 * result + Arrays.hashCode(array);
    return result;
  }
}
