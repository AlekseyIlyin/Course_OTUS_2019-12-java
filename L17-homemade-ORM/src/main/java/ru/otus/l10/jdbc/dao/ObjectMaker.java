package ru.otus.l10.jdbc.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ObjectMaker<T> {
    private final Class<T> tClass;
    private final Map<String, Object> values;

    public ObjectMaker(Class<T> tClass, Map<String, Object> values) {
        this.tClass = tClass;
        this.values = values;
    }

    public T createObject() {
        T object = null;
        Constructor<?>[] tConstructors = tClass.getConstructors();
        Constructor<?> tConstructor = null;
        for (Constructor<?> tConstr : tConstructors) {
            if (tConstr.getParameterCount() == 0) {
                tConstructor = tConstr;
                break;
            }
        }
        try {
            object = (T) tConstructor.newInstance();
            for (String s : values.keySet()) {
                final var declaredField = tClass.getDeclaredField(s);
                declaredField.setAccessible(true);

                declaredField.set(object, values.get(s));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return object;
    }
}
