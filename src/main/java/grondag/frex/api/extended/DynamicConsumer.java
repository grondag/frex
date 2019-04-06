package grondag.frex.api.extended;

@FunctionalInterface
public interface DynamicConsumer<T> {
    void accept(T emitter, RefreshSignal signal, int refreshFlags);
}
