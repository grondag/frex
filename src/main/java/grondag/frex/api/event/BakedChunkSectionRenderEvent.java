package grondag.frex.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class BakedChunkSectionRenderEvent {
    private static final Collection<Predicate<ChunkRenderConditionContext>> PREDICATES = new ArrayList<>();

    private static final Event<BakedChunkSectionRenderer> EVENT = EventFactory.createArrayBacked(BakedChunkSectionRenderer.class, listeners ->  context -> {
            for (BakedChunkSectionRenderer renderer : listeners) {
                renderer.bake(context);
            }
        }
    );

    public static void register(Predicate<ChunkRenderConditionContext> predicate, BakedChunkSectionRenderer renderer) {
        PREDICATES.add(predicate);

        EVENT.register(context -> {
            if (predicate.test(context)) {
                renderer.bake(context);
            }
        });
    }

    public static boolean shouldAnyFire(ChunkRenderConditionContext context) {
        for (Predicate<ChunkRenderConditionContext> predicate : PREDICATES) {
            if (predicate.test(context)) {
                return true;
            }
        }

        return false;
    }

    public static void invoke(ChunkRenderContext context) {
        EVENT.invoker().bake(context);
    }
}
