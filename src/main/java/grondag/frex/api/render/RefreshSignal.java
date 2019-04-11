package grondag.frex.api.render;

@FunctionalInterface
public interface RefreshSignal {
    public static final int PER_TICK_ONLY = 1;
    public static final int FIXED_OUTPUT_SIZE = 2;
    public static final int ALWAYS_EMITS = 4;
    
    boolean compute(int tickIndex, int frameIndex);
}
