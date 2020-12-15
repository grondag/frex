package grondag.frex.api.event;

import net.minecraft.util.Pair;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayBackedConditionalEvent<C, T> extends ConditionalEvent<C, T> {
    private final Function<Pair<Predicate<C>, T>[], T> invokerFactory;
    private final T dummyInvoker;
    private Pair<Predicate<C>, T>[] handlers;

    public ArrayBackedConditionalEvent(Function<Pair<Predicate<C>, T>[], T> invokerFactory) {
        this(invokerFactory, null);
    }

    public ArrayBackedConditionalEvent(Function<Pair<Predicate<C>, T>[], T> invokerFactory, T dummyInvoker) {
        this.invokerFactory = invokerFactory;
        this.dummyInvoker = dummyInvoker;
        this.update();
    }

    private void update() {
        if (handlers == null) {
            if (dummyInvoker != null) {
                invoker = dummyInvoker;
            } else {
                //noinspection unchecked
                invoker = invokerFactory.apply(
                        (Pair<Predicate<C>, T>[]) Array.newInstance(Pair.class, 0)
                );
            }
        } else {
            invoker = invokerFactory.apply(handlers);
        }
    }

    @Override
    public void register(Predicate<C> condition, T listener) {
        if (condition == null) {
            throw new NullPointerException("Tried to register a null condition!");
        }

        if (listener == null) {
            throw new NullPointerException("Tried to register a null listener!");
        }

        if (handlers == null) {
            //noinspection unchecked
            handlers = (Pair<Predicate<C>, T>[]) Array.newInstance(Pair.class, 1);
            handlers[0] = new Pair<>(condition, listener);
        } else {
            handlers = Arrays.copyOf(handlers, handlers.length + 1);
            handlers[handlers.length - 1] = new Pair<>(condition, listener);
        }

        update();
    }

    @Override
    public boolean shouldAnyFire(C context) {
        if (handlers == null) {
            return false;
        }

        update();

        for (Pair<Predicate<C>, T> pair : handlers) {
            if (pair.getLeft().test(context)) {
                return true;
            }
        }

        return false;
    }
}
