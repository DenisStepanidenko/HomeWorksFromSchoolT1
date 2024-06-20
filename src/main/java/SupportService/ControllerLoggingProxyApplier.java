package SupportService;

import SupportService.configuration.Logged;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ControllerLoggingProxyApplier implements ProxyApplier {
    @Override
    public Object apply(Object object) {
        var shouldProxyBeApplied = Arrays.stream(object.getClass().getInterfaces())
                .anyMatch(it -> it.isAnnotationPresent(Controller.class));

        if (shouldProxyBeApplied) {
            return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    object.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        if (method.isAnnotationPresent(Logged.class)) {
                            System.out.println("Старт метода");
                            var result = method.invoke(object, args);
                            System.out.println("Конец метода");
                            return result;
                        } else {
                            return method.invoke(object, args);
                        }
                    });
        } else {
            return object;
        }
    }
}
