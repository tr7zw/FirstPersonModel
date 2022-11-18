package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

@Mixin(Camera.class)
public class CameraMixin {
    
    @Inject(method = "setup", at = @At("TAIL"))
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        if(entity == Minecraft.getInstance().player && FirstPersonModelCore.config.offsetCamera && FirstPersonModelCore.enabled) {
            LocalPlayer player = (LocalPlayer) entity;
            float headYaw = Mth.rotLerp(f, player.yRotO, player.getYRot());
            float bodyYaw = Mth.rotLerp(f, player.yBodyRotO, player.yBodyRot);
            float offset = headYaw - bodyYaw;
            offset = Mth.clamp(offset, -50, 50);
            offset /= 500;
            offset *= FirstPersonModelCore.config.cameraOffsetMultiplier;
            move(0, 0, -offset);
        }
    }

    @Shadow
    protected void move(double d, double e, double f) {
        
    }
    
}
