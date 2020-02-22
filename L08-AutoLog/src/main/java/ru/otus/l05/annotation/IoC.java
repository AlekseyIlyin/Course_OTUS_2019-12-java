package ru.otus.l05.annotation;

import ru.otus.l05.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IoC {

    private static final List<Method> methodsWisAnnotation = new ArrayList<>(1);
    private static final Class<?> clazz = ClassProxyInterface.class;

    public static ClassProxyInterface createLoggerForClass() {
        InvocationHandler handler = new LogInvocationHandler(new TestLogging());

        methodsWisAnnotation.addAll( Arrays.stream(ClassProxyInterface.class.getMethods())
                .filter(method -> method.isAnnotationPresent(Log.class))
                .collect(Collectors.toList())
        );

        return (ClassProxyInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ClassProxyInterface.class}, handler);
    }

    static class LogInvocationHandler implements InvocationHandler {
        private final ClassProxyInterface logClass;

        LogInvocationHandler(ClassProxyInterface logClass) {
            this.logClass = logClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodsWisAnnotation.contains(method) ) {
                StringBuilder stringBuilder = new StringBuilder(String.format("executed method: %s, params: ", method.getName()));
                stringBuilder.append(Arrays.toString(args));
                System.out.println(stringBuilder.toString());
            }
            return method.invoke(logClass, args);
        }

        @Override
        public String toString() {
            return "LogInvocationHandler{" +
                    "Class=" + logClass +
                    '}';
        }
    }

}
