package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
//? if >= 1.21.2 {
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.client.renderer.state.*;
//? }
import net.minecraft.world.entity.player.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.world.entity.projectile.*;

@Mixin(FishingHookRenderer.class)
public class FishingBobberRendererMixin {

    private Vec3 offsetvec3d = Vec3.ZERO; // to not create @Nullable

    private boolean doCorrect() {
        return FirstPersonModelCore.instance.isEnabled()
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                && !FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems();
    }

    //? if <= 1.20.4 {
    /*
     @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getCameraType()Lnet/minecraft/client/CameraType;"))
    *///? } else {

    @Redirect(method = "getPlayerHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getCameraType()Lnet/minecraft/client/CameraType;"))
    //? }
    private CameraType redirect(Options gameOptions) {
        return doCorrect() ? CameraType.THIRD_PERSON_BACK : gameOptions.getCameraType();
    }

    //? if >= 1.21.9 {
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/FishingHookRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"))
    //? } else {
    /*@Inject(method = "render", at = @At("HEAD"))
    *///? }
       //? if >= 1.21.9 {

    public void render(FishingHookRenderState fishingHookRenderState, PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        //? } else if >= 1.21.3 {
        /*public void render(FishingHookRenderState fishingHookRenderState, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        *///? } else {
        /*
            private void calcOffset(FishingHook fishingBobberEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        *///? }
        if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            offsetvec3d = FirstPersonModelCore.instance.getLogicHandler().getOffset();// getPositionOffset((Player)
                                                                                      // fishingBobberEntity.getOwner(),
                                                                                      // matrixStack);
        } else {
            offsetvec3d = new Vec3(0, 0, 0);
        }
    }

    //? if <= 1.20.4 {
    /*
        @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getX()D"))
        private double offsetX(Player playerEntity) {
            return playerEntity.getX() + offsetvec3d.x();
        }
    
        @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getZ()D"))
        private double offsetZ(Player playerEntity) {
            return playerEntity.getZ() + offsetvec3d.z();
        }
    
        @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;xo:D"))
        private double prevOffsetX(Player playerEntity) {
            return playerEntity.xo + offsetvec3d.x();
        }
    
        @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;zo:D"))
        private double prevOffsetZ(Player playerEntity) {
            return playerEntity.zo + offsetvec3d.z();
        }
    *///? } else {

    @Inject(method = "getPlayerHandPos", at = @At("RETURN"), cancellable = true)
    private void getPlayerHandPosOffset(Player player, float f, float g, CallbackInfoReturnable<Vec3> ci) {
        ci.setReturnValue(ci.getReturnValue().add(offsetvec3d));
    }
    //? }

}
