package net.ririfa.mcefal.events;

import net.ririfa.beacon.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FrameBufferResizeEvent extends Event {
    public final int width;
    public final int height;

    public FrameBufferResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Contract("_, _ -> new")
    public static @NotNull FrameBufferResizeEvent get(int width, int height) {
        return new FrameBufferResizeEvent(width, height);
    }
}
