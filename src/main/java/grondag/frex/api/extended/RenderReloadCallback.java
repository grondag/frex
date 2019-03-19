package grondag.frex.api.extended;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RenderReloadCallback {
    public static final Event<RenderReloadCallback> EVENT = EventFactory.createArrayBacked(RenderReloadCallback.class,
        (listeners) -> {
            return () -> {
                for (RenderReloadCallback event : listeners) {
                    event.reload();
                }
            };
        }
    );

    void reload();
}
