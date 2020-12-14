package grondag.frex.api.event;

import java.util.function.Function;

public abstract class ConditionalEvent<C, T> {
    protected T invoker;

    /**
     * Returns the invoker instance.
     *
     * <p>An "invoker" is an object which hides multiple registered
     * listeners of type T under one instance of type T, executing
     * them and leaving early as necessary.
     *
     * @return The invoker instance.
     */
    public final T invoker() {
        return invoker;
    }

    /**
     * Register a listener to the event.
     *
     * @param condition Condition under which the listener should be fired.
     * @param listener The desired listener.
     */
    public abstract void register(Function<C, Boolean> condition, T listener);

    /**
     * Whether any of the registered conditions are true.
     *
     * @param context Context to pass to the conditions.
     * @return True if any of the registered conditions are true, false otherwise.
     */
    public abstract boolean shouldAnyFire(C context);
}