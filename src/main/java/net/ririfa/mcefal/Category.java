package net.ririfa.mcefal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record Category(String name) {
    private static final Map<String, Category> REGISTRY = new HashMap<>();

    public Category(String name) {
        this.name = name;
        REGISTRY.put(name, this);
    }

    public static Category getByName(String name) {
        return REGISTRY.get(name);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Map<String, Category> getAllCategories() {
        return new HashMap<>(REGISTRY);
    }
}
