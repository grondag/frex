package grondag.frex.api;

/**
 * Implement and register with renderer via {@link ExtendedRenderer#registerListener(RenderListener)}.
 */
public interface RenderListener {
    /**
     * Called when rendered chunks, shaders, etc. are reloaded, due to a
     * configuration change, resource pack change, or user pressing F3 + A;
     * 
     * TODO: make this an event
     */
    public default void onRenderReload() {
    };
}
