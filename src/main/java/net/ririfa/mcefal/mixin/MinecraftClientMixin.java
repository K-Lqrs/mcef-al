package net.ririfa.mcefal.mixin;

import net.minecraft.client.MinecraftClient;
import net.ririfa.beacon.EventBus;
import net.ririfa.mcefal.events.ResourceManagerResolveEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ResourcePackManager;<init>([Lnet/minecraft/resource/ResourcePackProvider;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onResourceManagerResolved(CallbackInfo ci) {
        EventBus.postFullSync(ResourceManagerResolveEvent.get());
    }
}
