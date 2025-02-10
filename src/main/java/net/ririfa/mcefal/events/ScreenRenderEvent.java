package net.ririfa.mcefal.events;

import net.minecraft.client.gui.DrawContext;
import net.ririfa.beacon.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ScreenRenderEvent extends Event {
    public final DrawContext context;
    public final float partialTicks;

    private ScreenRenderEvent(
            DrawContext context,
            float partialTicks
    ) {
        this.context = context;
        this.partialTicks = partialTicks;
    }

    @Contract("_, _ -> new")
    public static @NotNull ScreenRenderEvent get(
            DrawContext context,
            float partialTicks
    ) {
        return new ScreenRenderEvent(context, partialTicks);
    }
}
