package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;

@Mixin(FishingHookRenderer.class)
public class FishingBobberRendererMixin {

    private Vec3 offsetvec3d = Vec3.ZERO; // to not create @Nullable

    private boolean doCorrect() {
        return FirstPersonModelCore.enabled
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getCameraType()Lnet/minecraft/client/CameraType;"))
    private CameraType redirect(Options gameOptions) {
        return (doCorrect()) ? CameraType.THIRD_PERSON_BACK : gameOptions.getCameraType();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void calcOffset(FishingHook fishingBobberEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        if (fishingBobberEntity.getOwner() instanceof Player) {
            this.offsetvec3d = FirstPersonModelCore.instance.getLogicHandler().getOffset();// getPositionOffset((Player)
                                                                                           // fishingBobberEntity.getOwner(),
                                                                                           // matrixStack);
        } else {
            this.offsetvec3d = new Vec3(0, 0, 0);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getX()D"))
    private double offsetX(Player playerEntity) {
        return playerEntity.getX() + this.offsetvec3d.x();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getZ()D"))
    private double offsetZ(Player playerEntity) {
        return playerEntity.getZ() + this.offsetvec3d.z();
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;xo:D"))
    private double prevOffsetX(Player playerEntity) {
        return playerEntity.xo + offsetvec3d.x();
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;zo:D"))
    private double prevOffsetZ(Player playerEntity) {
        return playerEntity.zo + offsetvec3d.z();
    }

}
