package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.api.PlayerOffsetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;

/**
 * Offset the player behind the camera
 * 
 * @author tr7zw
 *
 */
@Mixin(value = PlayerRenderer.class, priority = 500)
public class PlayerRendererMixin {

    private static Minecraft fpmMcInstance = Minecraft.getInstance();

    @Inject(method = "getRenderOffset", at = @At("RETURN"), cancellable = true)
    public void getRenderOffset(AbstractClientPlayer entity, float delta, CallbackInfoReturnable<Vec3> ci) {
        if (entity == fpmMcInstance.cameraEntity && FirstPersonModelCore.instance.isRenderingPlayer()) {
            FirstPersonModelCore.instance.getLogicHandler().updatePositionOffset(entity, Vec3.ZERO, delta);

            Vec3 offset = ci.getReturnValue().add(FirstPersonModelCore.instance.getLogicHandler().getOffset());

            for (PlayerOffsetHandler handler : FirstPersonAPI.getPlayerOffsetHandlers()) {
                offset = handler.applyOffset(entity, delta, ci.getReturnValue(), offset);
            }

            ci.setReturnValue(offset);
        }
    }

}
