package grondag.frex.api.material;

import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface MaterialCondition {
	/**
	 * Called at most once per frame.
	 */
	boolean compute();
}
