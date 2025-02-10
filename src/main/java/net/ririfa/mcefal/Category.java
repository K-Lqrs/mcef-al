package net.ririfa.mcefal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a category for organising browser tabs or other elements within the system.
 * <p>
 * Each category is uniquely identified by its name and is registered in a global registry.
 * This allows retrieval and management of categories through static methods.
 */
public record Category(String name) {
    private static final Map<String, Category> REGISTRY = new HashMap<>();

    /**
     * Constructs a new {@code Category} with the given name and registers it in the global registry.
     *
     * @param name The unique name of the category.
     */
    public Category(String name) {
        this.name = name;
        REGISTRY.put(name, this);
    }

    /**
     * Retrieves a {@code Category} from the registry by its name.
     *
     * @param name The name of the category to retrieve.
     * @return The corresponding {@code Category} if found, otherwise {@code null}.
     */
    public static Category getByName(String name) {
        return REGISTRY.get(name);
    }

    /**
     * Returns a map containing all registered categories.
     *
     * @return A new map containing all categories registered in the system.
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull Map<String, Category> getAllCategories() {
        return new HashMap<>(REGISTRY);
    }
}
