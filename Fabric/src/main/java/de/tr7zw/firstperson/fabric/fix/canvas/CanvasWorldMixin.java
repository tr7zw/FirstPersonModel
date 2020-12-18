package de.tr7zw.firstperson.fabric.fix.canvas;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelCore;
import grondag.canvas.render.CanvasWorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Mixin(CanvasWorldRenderer.class)
public class CanvasWorldMixin {
    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"))
    private void isThirdPerson(MatrixStack matrixStack, float tickDelta, long limitTime, boolean blockOutlines, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci){
        FirstPersonModelCore.instance.getWrapper().isThirdPersonTrigger(matrixStack);
    }
}
