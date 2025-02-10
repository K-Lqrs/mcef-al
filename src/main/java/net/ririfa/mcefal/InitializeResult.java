package net.ririfa.mcefal;

import net.ccbluex.liquidbounce.mcef.MCEFResourceManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the result of initializing the MCEF (Minecraft Chromium Embedded Framework).
 * This record stores whether the initialization was successful and provides access to the
 * {@link MCEFResourceManager}, which manages MCEF resources.
 *
 * @param result Indicates whether the MCEF initialization was successful.
 * @param resourceManager The resource manager associated with MCEF.
 */
@SuppressWarnings("unused")
public record InitializeResult(
        boolean result,
        MCEFResourceManager resourceManager
) {
    /**
     * Checks if the initialization was successful.
     *
     * @return {@code true} if MCEF was initialized successfully, otherwise {@code false}.
     */
    public boolean isSuccessful() {
        return result;
    }

    /**
     * Checks if the resource manager is available.
     *
     * @return {@code true} if a valid {@link MCEFResourceManager} is present, otherwise {@code false}.
     */
    public boolean hasResourceManager() {
        return resourceManager != null;
    }

    /**
     * Retrieves the resource manager if available.
     *
     * @return The {@link MCEFResourceManager} instance.
     * @throws IllegalStateException If the resource manager is not available.
     */
    public MCEFResourceManager getResourceManagerOrThrow() {
        if (resourceManager == null) {
            throw new IllegalStateException("Resource Manager is not available.");
        }
        return resourceManager;
    }

    /**
     * Returns a string representation of the initialization result.
     *
     * @return A formatted string containing the result status and resource manager information.
     */
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "InitializeResult{" +
                "result=" + result +
                ", resourceManager=" + (resourceManager != null ? "Available" : "Not Available") +
                '}';
    }
}
