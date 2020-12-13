package de.tr7zw.firstperson.mixins;


import net.minecraft.client.options.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberRendererMixin {

    private boolean doCorrect(){
        return FirstPersonModelMod.enabled && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON;
    }

    private MinecraftClient client = MinecraftClient.getInstance();
    private Vec3d offsetvec3d = Vec3d.ZERO; //to not create @Nullable

    private Vec3d getPositionOffset(PlayerEntity var1, MatrixStack matrices) {
        double x,y,z = x = y = z = 0;
        PlayerEntity abstractClientPlayerEntity_1;
        Float realYaw = 0f;
        if(FirstPersonModelMod.isFixActive(var1, matrices) && doCorrect()) {
            abstractClientPlayerEntity_1 = (PlayerEntity) var1;
            realYaw = MathHelper.lerpAngleDegrees(MinecraftClient.getInstance().getTickDelta(), abstractClientPlayerEntity_1.prevYaw, abstractClientPlayerEntity_1.yaw);
        }else {
            abstractClientPlayerEntity_1 = null;
            return Vec3d.ZERO;
        }
        if (abstractClientPlayerEntity_1 != null && (!abstractClientPlayerEntity_1.isMainPlayer() || MinecraftClient.getInstance().getCameraEntity() != null && MinecraftClient.getInstance().getCameraEntity() == abstractClientPlayerEntity_1)) {
            float bodyOffset;
            if(abstractClientPlayerEntity_1.isSneaking()){
                bodyOffset = FirstPersonModelMod.sneakBodyOffset + (FirstPersonModelMod.config.firstPerson.sneakXOffset / 100f);
            }else if(client.player.isInSwimmingPose()) {
                abstractClientPlayerEntity_1.bodyYaw = abstractClientPlayerEntity_1.headYaw;
                if(abstractClientPlayerEntity_1.prevPitch > 0) {
                    bodyOffset = FirstPersonModelMod.swimUpBodyOffset;
                }else {
                    bodyOffset = FirstPersonModelMod.swimDownBodyOffset;
                }
            }else if(abstractClientPlayerEntity_1.hasVehicle()) {
                bodyOffset = FirstPersonModelMod.inVehicleBodyOffset;
            }else{
                bodyOffset = 0.25f + (FirstPersonModelMod.config.firstPerson.xOffset / 100f);
            }
            x += bodyOffset * Math.sin(Math.toRadians(realYaw));
            z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
            if(client.player.isInSwimmingPose()) {
                if(abstractClientPlayerEntity_1.prevPitch > 0  && abstractClientPlayerEntity_1.isSubmergedInWater()) {
                    y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
                }else {
                    y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
                }
            }

        }
        Vec3d vec = new Vec3d(x, y, z);
        abstractClientPlayerEntity_1 = null;
        FirstPersonModelMod.isRenderingPlayer = false;
        return vec;
    }

    @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/options/GameOptions;getPerspective()Lnet/minecraft/client/options/Perspective;"
    ))
    private Perspective redirect(GameOptions gameOptions){
        return (doCorrect()) ? Perspective.THIRD_PERSON_BACK : gameOptions.getPerspective();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void calcOffset(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info){
        if(fishingBobberEntity.getOwner() instanceof PlayerEntity) {
        	this.offsetvec3d = getPositionOffset((PlayerEntity)fishingBobberEntity.getOwner(), matrixStack);
        }else {
        	this.offsetvec3d = new Vec3d(0, 0, 0);
        }
    }

    @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getX()D"
    ))
    private double offsetX(PlayerEntity playerEntity){
        return playerEntity.getX() + this.offsetvec3d.getX();
    }

    @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getZ()D"
    ))
    private double offsetZ(PlayerEntity playerEntity){
        return playerEntity.getZ() + this.offsetvec3d.getZ();
    }

    @Redirect(method = "render", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerEntity;prevX:D"
    ))
    private double prevOffsetX(PlayerEntity playerEntity){
        return playerEntity.prevX + offsetvec3d.getX();
    }
    @Redirect(method = "render", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerEntity;prevZ:D"
    ))
    private double prevOffsetZ(PlayerEntity playerEntity){
        return playerEntity.prevZ + offsetvec3d.getZ();
    }


}
