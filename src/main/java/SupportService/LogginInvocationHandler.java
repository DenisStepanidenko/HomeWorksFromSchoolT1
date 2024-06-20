package SupportService;

import SupportService.configuration.Logged;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogginInvocationHandler implements InvocationHandler {

    private final Object target;

    public LogginInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Logged.class)) {
            System.out.println("Старт метода");
            var result = method.invoke(target, args);
            System.out.println("Конец метода");
            return result;
        } else {
            return method.invoke(target, args);
        }


    }
}
