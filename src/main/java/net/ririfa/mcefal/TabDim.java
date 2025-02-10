package net.ririfa.mcefal;

import net.ccbluex.liquidbounce.mcef.MCEF;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record TabDim(
        int x,
        int y,
        int width,
        int height,
        boolean fullscreen
) {
    @Contract(" -> new")
    public static @NotNull TabDim fullScreen() {
        return new TabDim(
                0,
                0,
                MCEF.mc.getWindow().getFramebufferWidth(),
                MCEF.mc.getWindow().getFramebufferHeight(),
                true
        );
    }
}
