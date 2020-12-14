package grondag.frex.api.event;

import net.minecraft.util.Pair;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

public class ArrayBackedConditionalEvent<C, T> extends ConditionalEvent<C, T> {
    private final Function<Pair<Function<C, Boolean>, T>[], T> invokerFactory;
    private final T dummyInvoker;
    private Pair<Function<C, Boolean>, T>[] handlers;

    public ArrayBackedConditionalEvent(Function<Pair<Function<C, Boolean>, T>[], T> invokerFactory) {
        this(invokerFactory, null);
    }

    public ArrayBackedConditionalEvent(Function<Pair<Function<C, Boolean>, T>[], T> invokerFactory, T dummyInvoker) {
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
                        (Pair<Function<C, Boolean>, T>[]) Array.newInstance(Pair.class, 0)
                );
            }
        } else if (handlers.length == 1) {
            invoker = handlers[0].getRight();
        } else {
            invoker = invokerFactory.apply(handlers);
        }
    }

    @Override
    public void register(Function<C, Boolean> condition, T listener) {
        if (condition == null) {
            throw new NullPointerException("Tried to register a null condition!");
        }

        if (listener == null) {
            throw new NullPointerException("Tried to register a null listener!");
        }

        if (handlers == null) {
            //noinspection unchecked
            handlers = (Pair<Function<C, Boolean>, T>[]) Array.newInstance(Pair.class, 1);
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

        for (Pair<Function<C, Boolean>, T> pair : handlers) {
            if (pair.getLeft().apply(context)) {
                return true;
            }
        }

        return false;
    }
}
