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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;

@Mixin(FishingHookRenderer.class)
public class FishingBobberRendererMixin {

    private boolean doCorrect() {
        return FirstPersonModelCore.enabled
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
    }

    private Minecraft client = Minecraft.getInstance();
    private Vec3 offsetvec3d = Vec3.ZERO; // to not create @Nullable

    private Vec3 getPositionOffset(Player var1, PoseStack matrices) {
        double x, y, z = x = y = z = 0;
        Player abstractClientPlayerEntity_1;
        Float realYaw = 0f;
        if (FirstPersonModelCore.isRenderingPlayer && doCorrect()) {
            abstractClientPlayerEntity_1 = (Player) var1;
            realYaw = Mth.rotLerp(Minecraft.getInstance().getFrameTime(), abstractClientPlayerEntity_1.yRotO,
                    abstractClientPlayerEntity_1.getYRot());
        } else {
            abstractClientPlayerEntity_1 = null;
            return Vec3.ZERO;
        }
        if (abstractClientPlayerEntity_1 != null
                && (!abstractClientPlayerEntity_1.isLocalPlayer() || Minecraft.getInstance().getCameraEntity() != null
                        && Minecraft.getInstance().getCameraEntity() == abstractClientPlayerEntity_1)) {
            float bodyOffset;
            if (abstractClientPlayerEntity_1.isShiftKeyDown()) {
                bodyOffset = FirstPersonModelCore.sneakBodyOffset + (FirstPersonModelCore.config.sneakXOffset / 100f);
            } else if (client.player.isVisuallySwimming()) {
                abstractClientPlayerEntity_1.yBodyRot = abstractClientPlayerEntity_1.yHeadRot;
                if (abstractClientPlayerEntity_1.xRotO > 0) {
                    bodyOffset = FirstPersonModelCore.swimUpBodyOffset;
                } else {
                    bodyOffset = FirstPersonModelCore.swimDownBodyOffset;
                }
            } else if (abstractClientPlayerEntity_1.isPassenger()) {
                bodyOffset = FirstPersonModelCore.inVehicleBodyOffset;
            } else {
                bodyOffset = 0.25f + (FirstPersonModelCore.config.xOffset / 100f);
            }
            x += bodyOffset * Math.sin(Math.toRadians(realYaw));
            z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
            if (client.player.isVisuallySwimming()) {
                if (abstractClientPlayerEntity_1.xRotO > 0 && abstractClientPlayerEntity_1.isUnderWater()) {
                    y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.xRotO));
                } else {
                    y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.xRotO));
                }
            }

        }
        Vec3 vec = new Vec3(x, y, z);
        abstractClientPlayerEntity_1 = null;
        FirstPersonModelCore.isRenderingPlayer = false;
        return vec;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getCameraType()Lnet/minecraft/client/CameraType;"))
    private CameraType redirect(Options gameOptions) {
        return (doCorrect()) ? CameraType.THIRD_PERSON_BACK : gameOptions.getCameraType();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void calcOffset(FishingHook fishingBobberEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        if (fishingBobberEntity.getOwner() instanceof Player) {
            this.offsetvec3d = getPositionOffset((Player) fishingBobberEntity.getOwner(), matrixStack);
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
