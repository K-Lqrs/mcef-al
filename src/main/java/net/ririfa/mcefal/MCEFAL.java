package net.ririfa.mcefal;

import net.ccbluex.liquidbounce.mcef.MCEF;
import net.ccbluex.liquidbounce.mcef.MCEFResourceManager;
import net.ririfa.beacon.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

/**
 * MCEFAL (Minecraft Chromium Embedded Framework Abstraction Layer).
 * This enum acts as a singleton wrapper around {@link MCEF}, providing
 * simplified initialization and management of MCEF resources.
 */
@SuppressWarnings("unused")
public enum MCEFAL {
    INSTANCE;

    String[] packageName;

    MCEFAL() {
    }

    /**
     * Initializes the MCEF library and sets up the necessary resources.
     * Downloads JCEF if necessary.
     *
     * @return An {@link InitializeResult} containing the initialization status and resource manager.
     * @throws IOException If an error occurs during initialization or JCEF download fails.
     * @throws IllegalStateException If MCEF is already initialized.
     */
    public synchronized @NotNull InitializeResult initialize(String[] packageName) throws IOException {
        if (MCEF.INSTANCE.isInitialized()) {
            throw new IllegalStateException("MCEF is already initialized");
        }

        this.packageName = packageName;

        EventBus.initialize(new String[]{"net.rk4z.mcefal"}, 2);

        MCEFResourceManager resMan = Optional.ofNullable(MCEF.INSTANCE.getResourceManager())
                .orElseGet(() -> {
                    try {
                        return MCEF.INSTANCE.newResourceManager();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create MCEF Resource Manager", e);
                    }
                });

        if (resMan.requiresDownload()) {
            resMan.downloadJcef();
        } else {
            MCEF.INSTANCE.getLogger().info("Skipping JCEF download as it is already present.");
        }

        boolean initRes = MCEF.INSTANCE.initialize();
        return new InitializeResult(initRes, resMan);
    }

    /**
     * Shuts down MCEF if it is initialized.
     *
     * @throws IllegalStateException If MCEF is not initialized.
     */
    public synchronized void shutdown() {
        if (!MCEF.INSTANCE.isInitialized()) {
            throw new IllegalStateException("MCEF is not initialized");
        }
        MCEF.INSTANCE.shutdown();
    }

    /**
     * Reloads the MCEF resources if it is initialized.
     * This shuts down MCEF first, then reinitializes it.
     *
     * @return An {@link InitializeResult} containing the initialization status and resource manager.
     * @throws RuntimeException If the reinitialization fails.
     */
    public synchronized @NotNull InitializeResult reload() {
        boolean wasInitialized = MCEF.INSTANCE.isInitialized();

        if (wasInitialized) {
            shutdown();
        }

        try {
            return initialize(packageName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to reload MCEF", e);
        }
    }

    /**
     * Returns the singleton instance of {@link MCEF}.
     *
     * @return The {@link MCEF} instance.
     */
    public MCEF getMCEF() {
        return MCEF.INSTANCE;
    }

    /**
     * Retrieves the version (commit hash) of the embedded CEF (Chromium Embedded Framework).
     * This version represents the specific CEF commit used by MCEF.
     *
     * @return The CEF version as a {@link String}.
     * @throws IOException If an error occurs while fetching the CEF version.
     */
    public String getCefVersion() throws IOException {
        return MCEF.INSTANCE.getJavaCefCommit();
    }

    /**
     * Checks if MCEF is fully initialized and ready to be used.
     *
     * @return {@code true} if MCEF is initialized and resources are loaded, otherwise {@code false}.
     */
    public boolean isReady() {
        return MCEF.INSTANCE.isInitialized() && MCEF.INSTANCE.getResourceManager() != null;
    }
}
