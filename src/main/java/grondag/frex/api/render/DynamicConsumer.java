package grondag.frex.api.render;

@FunctionalInterface
public interface DynamicConsumer<T> {
    void accept(T emitter, RefreshSignal signal, int refreshFlags);
}
