package ru.otus.l10.core.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ObjectConstructor<T> {

    public T makeObject(Class<T> tClass, Map<String, Object> values) {
        T object = null;
        try {
            object = tClass.getConstructor().newInstance();
            for (String s : values.keySet()) {
                final var declaredField = tClass.getDeclaredField(s);
                declaredField.setAccessible(true);
                declaredField.set(object, values.get(s));
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return object;
    }

}
