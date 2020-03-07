package ru.otus.l05.annotation;

import ru.otus.l05.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class IoC {

    private static final Set<Method> methodsWisAnnotation = new HashSet<>(1);
    private static final Class<?> clazz = TestLogging.class;

    public static ClassProxyInterface createLoggerForClass() {
        InvocationHandler handler = new LogInvocationHandler(new TestLogging());

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                for (Method piMethod : ClassProxyInterface.class.getDeclaredMethods()) {
                    if (isEqualsMethods(method,piMethod)) {
                        methodsWisAnnotation.add(piMethod);
                        break;
                    }
                }
            }
        }

        return (ClassProxyInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ClassProxyInterface.class}, handler);
    }

    private static boolean isEqualsMethods(Method method_1, Method method_2) {
        boolean isEquals = (method_1.getName().equals(method_2.getName()));
        if (isEquals && method_1.getParameterCount() == method_2.getParameterCount()) {
            Class<?>[] method_1_params = method_1.getParameterTypes();
            Class<?>[] method_2_params = method_2.getParameterTypes();
            for (int paramCount = 0; paramCount < method_1.getParameterCount(); paramCount++) {
                if (method_1_params[paramCount] != method_2_params[paramCount]) {
                    isEquals = false;
                    break;
                }
            }

        } else {
            isEquals = false;
        }
        return isEquals;
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
