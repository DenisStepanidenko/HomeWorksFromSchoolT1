package SupportService;

import SupportService.configuration.Configuration;
import SupportService.configuration.Instance;
import SupportService.configuration.Logged;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {
    private final Map<String, Object> instances = new HashMap<>();
    private final List<Method> instanceMethods = new ArrayList<>();
    private final Map<Class<?>, Object> configInstances = new HashMap<>();
    private final Set<String> instancesInProgress = new HashSet<>();

    public ApplicationContext(String packageName) {
        scanConfigurationClasses(packageName);
        init();

    }

    private void init() {
        instanceMethods.stream()
                .sorted(Comparator.comparingInt((Method method) -> method.getAnnotation(Instance.class).priority()).reversed())
                .forEach(method -> createInstance(method.getName(), method));
    }

    private Object createInstance(String instanceName, Method method) {
        if (instancesInProgress.contains(instanceName)) {
            throw new RuntimeException("Circular dependency detected for instance: " + instanceName);
        }

        instancesInProgress.add(instanceName);

        try {
            Object configInstance = configInstances.get(method.getDeclaringClass());
            Object[] dependencies = resolveDependencies(method);
            Object instance = method.invoke(configInstance, dependencies);

            instances.put(instanceName , applyProxies(instance));
            return instance;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create instance: " + instanceName, e);
        } finally {
            instancesInProgress.remove(instanceName);
        }

    }

    public <T> List<T> getInstances(Class<T> instanceType){
        return (List<T>) instances.values().stream()
                .filter(instanceType::isInstance)
                .toList();
    }


    private Object applyProxies(Object object) {
        Object result = object;
        for(ProxyApplier applier : getInstances(ProxyApplier.class)){
            result = applier.apply(result);
        }

        return result;
    }

    private Object[] resolveDependencies(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(this::getInstance)
                .toArray();
    }

    public <T> T getInstance(Class<T> instanceType) {
        return instanceType.cast(instances.values().stream()
                .filter(instanceType::isInstance)
                .findFirst()
                .orElseGet(() -> createInstancesByType(instanceType)));
    }

    private Object createInstancesByType(Class<?> instanceType) {
        for(Method method : instanceMethods){
            if(instanceType.isAssignableFrom(method.getReturnType())){
                return createInstance(method.getName() , method);
            }
        }
        throw new RuntimeException("No instance found of type " + instanceType.getName());
    }

    private void scanConfigurationClasses(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> configurationsClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        for (Class<?> configClass : configurationsClasses) {
            try {
                Object configInstance = configClass.getDeclaredConstructor().newInstance();
                configInstances.put(configClass, configInstance);

                for (Method method : configClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Instance.class)) {
                        instanceMethods.add(method);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }


//    private final Map<Class<?>, Object> instances = new HashMap<>();
//
//    public ApplicationContext() throws InvocationTargetException, IllegalAccessException {
//        Reflections reflections = new Reflections("SupportService.configuration");
//
//        var configurations = reflections.getTypesAnnotatedWith(Configuration.class).
//                stream()
//                .map(type -> {
//                    try {
//                        return type.getDeclaredConstructor().newInstance();
//                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
//                             NoSuchMethodException e) {
//                        throw new RuntimeException(e);
//                    }
//                }).toList();
//
//        for (Object configuration : configurations) {
//            List<Method> methods = Arrays.stream(configuration.getClass().getMethods())
//                    .filter(method -> method.isAnnotationPresent(Instance.class)).toList();
//
//            List<Method> methodsWithoutParams = methods.stream()
//                    .filter(method -> method.getParameters().length == 0)
//                    .toList();
//
//
//            List<Method> methodWithParameters = methods.stream()
//                    .filter(method -> method.getParameters().length > 0)
//                    .toList();
//
//            for (Method methodsWithoutParam : methodsWithoutParams) {
//                var instance = wrapWithLoggingProxy(methodsWithoutParam.invoke(configuration));
//
//
//                instances.put(methodsWithoutParam.getReturnType(), instance);
//            }
//
//            for (Method methodWithParameter : methodWithParameters) {
//                Object[] objects = Arrays.stream(methodWithParameter.getParameters())
//                        .map(param -> instances.get(param.getType()))
//                        .toArray();
//
//                var instance = wrapWithLoggingProxy(methodWithParameter.invoke(configuration, objects));
//
//                instances.put(methodWithParameter.getReturnType(), instance);
//            }
//
//        }
//    }
//
//    private Object wrapWithLoggingProxy(Object instance) {
//        return Proxy.newProxyInstance(this.getClass().getClassLoader(), instance.getClass().getInterfaces(), new LogginInvocationHandler(instance));
//    }
//
//
//    public <T> T getInstance(Class<T> type) {
//        return (T) Optional.ofNullable(instances.get(type)).orElseThrow();
//    }
}
