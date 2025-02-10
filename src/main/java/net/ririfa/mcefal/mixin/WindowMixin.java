package net.ririfa.mcefal.mixin;

import net.minecraft.client.util.Window;
import net.ririfa.beacon.EventBus;
import net.ririfa.mcefal.events.FrameBufferResizeEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    @Shadow
    @Final
    private long handle;

    @Inject(method = "onFramebufferSizeChanged", at = @At("RETURN"))
    public void hookFramebufferResize(long window, int width, int height, CallbackInfo callbackInfo) {
        if (window == handle) {
            EventBus.postFullSync(FrameBufferResizeEvent.get(width, height));
        }
    }
}
