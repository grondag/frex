package grondag.frex.api.render;

import org.apiguardian.api.API;

@FunctionalInterface
@API(status = API.Status.EXPERIMENTAL)
public interface DynamicConsumer<T> {
    void accept(T emitter, RefreshSignal signal, int refreshFlags);
}
