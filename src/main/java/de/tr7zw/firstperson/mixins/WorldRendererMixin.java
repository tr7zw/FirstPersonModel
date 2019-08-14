package de.tr7zw.firstperson.mixins;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VisibleRegion;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyArg(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;FZ)V"), index = 0)
    private Entity preRenderPlayer(Entity rendered) {
        if (MinecraftClient.getInstance().options.perspective == 0 && rendered == client.cameraEntity) {
            FirstPersonModelMod.renderingFirstPersonPlayer = true;
        }
        return rendered;
    }

    @Inject(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;FZ)V", shift = At.Shift.AFTER))
    private void postRenderPlayer(Camera camera, VisibleRegion visibleRegion, float tickDelta, CallbackInfo ci) {
        FirstPersonModelMod.renderingFirstPersonPlayer = false;
    }
}
