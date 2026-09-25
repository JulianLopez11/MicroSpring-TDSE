package co.edu.retailgo.minispring;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MiniContainer {
    private final List<Class<?>> components;
    private final Map<Class<?>, Object> singletons = new LinkedHashMap<>();
    private final Deque<Class<?>> creationPath = new ArrayDeque<>();

    public MiniContainer(Class<?>... candidates) {
        this.components = Arrays.stream(candidates)
                .filter(type -> type.isAnnotationPresent(RGComponent.class))
                .toList();
    }

    public <T> T getBean(Class<T> requestedType) {
        Object existing = findExisting(requestedType);
        if (existing != null) {
            return requestedType.cast(existing);
        }

        Class<?> implementation = resolveImplementation(requestedType);
        Object bean = createBean(implementation);
        return requestedType.cast(bean);
    }

    private Object findExisting(Class<?> requestedType) {
        return singletons.entrySet().stream()
                .filter(entry -> requestedType.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private Class<?> resolveImplementation(Class<?> requestedType) {
        List<Class<?>> matches = components.stream()
                .filter(requestedType::isAssignableFrom)
                .toList();

        if (matches.isEmpty()) {
            throw new ContainerException(
                    "No existe componente para " + requestedType.getName()
            );
        }

        if (matches.size() > 1) {
            throw new ContainerException(
                    "Existe más de un componente para " + requestedType.getName()
            );
        }

        return matches.get(0);
    }

    private Object createBean(Class<?> implementation) {
        Object existing = singletons.get(implementation);
        if (existing != null) {
            return existing;
        }

        if (creationPath.contains(implementation)) {
            throw new ContainerException(
                    "Dependencia circular detectada: " + creationPath
                            + " -> " + implementation.getSimpleName()
            );
        }

        creationPath.addLast(implementation);

        try {
            Constructor<?> constructor = selectConstructor(implementation);

            Object[] dependencies = Arrays.stream(constructor.getParameterTypes())
                    .map(this::getBean)
                    .toArray();

            Object bean = constructor.newInstance(dependencies);
            singletons.put(implementation, bean);
            return bean;

        } catch (InvocationTargetException e) {
            throw new ContainerException(
                    "Error al crear " + implementation.getName(),
                    e.getCause()
            );
        } catch (ReflectiveOperationException e) {
            throw new ContainerException(
                    "No fue posible crear " + implementation.getName(),
                    e
            );
        } finally {
            creationPath.removeLast();
        }
    }

    private Constructor<?> selectConstructor(Class<?> implementation) {
        List<Constructor<?>> injectableConstructors =
                Arrays.stream(implementation.getDeclaredConstructors())
                        .filter(constructor ->
                                constructor.isAnnotationPresent(RGInject.class))
                        .toList();

        if (injectableConstructors.size() == 1) {
            return injectableConstructors.get(0);
        }

        if (injectableConstructors.size() > 1) {
            throw new ContainerException(
                    "Hay más de un constructor con @RGInject en "
                            + implementation.getName()
            );
        }

        Constructor<?>[] constructors = implementation.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        throw new ContainerException(
                "No se puede elegir constructor para " + implementation.getName()
        );
    }
}
