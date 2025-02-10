package net.ririfa.mcefal.events;

import net.ririfa.beacon.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ResourceManagerResolveEvent extends Event {
    @Contract(value = " -> new", pure = true)
    public static @NotNull ResourceManagerResolveEvent get() {
        return new ResourceManagerResolveEvent();
    }
}
