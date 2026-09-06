package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.access.*;
import lombok.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
//? if >= 26.1 {
import net.minecraft.client.renderer.state.level.*;
//? }
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.world.entity.*;

//? if < 1.21.3 {
/*import org.spongepowered.asm.mixin.injection.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
*///? }

/**
 * Disable hitbox rendering for the player in first person mode
 *
 */
@Mixin(EntityRenderDispatcher.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RenderDispatcherMixin {

    private static Minecraft fpmMcInstance = Minecraft.getInstance();

    //? if >= 26.1 {

    private boolean fpmSubmittingCameraEntity = false;

    /**
     * YSM (and potentially other mods) defer their animation evaluation to the
     * submit of the render state and check {@link FirstPersonModelCore#isRenderingPlayer()}
     * at that point to decide when to hide the head. Two windows keep the flag
     * up for exactly those moments:
     * - extraction of the camera entity: widens the flag (without marking the
     *   states themselves, so third party captures stay unflagged) which makes
     *   YSM attach its first person context and re-evaluate the head visibility
     *   on every submit,
     * - the submit of the state FPM extracted (marked via isCameraEntity): the
     *   only evaluation that must see the flag up, hiding the head for the
     *   actual frame render. Evaluations triggered by other mods (RealCamera,
     *   GUIs) run with the flag down and keep the model headed.
     */
    @Inject(method = "extractEntity(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;",
            at = @At("HEAD"), expect = 0, require = 0)
    private void fpmExtractStart(Entity entity, float f, CallbackInfoReturnable<EntityRenderState> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.getCameraEntity() && mc.options.getCameraType() == CameraType.FIRST_PERSON
                && FirstPersonModelCore.instance.getLogicHandler().shouldApplyThirdPerson(false)) {
            FirstPersonModelCore.cameraEntityExtract = true;
        }
    }

    @Inject(method = "extractEntity(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;",
            at = @At("TAIL"), expect = 0, require = 0)
    private void fpmExtractEnd(Entity entity, float f, CallbackInfoReturnable<EntityRenderState> cir) {
        FirstPersonModelCore.cameraEntityExtract = false;
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/level/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
            at = @At("HEAD"), expect = 0, require = 0)
    private void fpmSubmitStart(EntityRenderState renderState, CameraRenderState camera, double x, double y, double z,
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (renderState instanceof LivingEntityRenderState livingState
                && ((LivingEntityRenderStateAccess) (Object) livingState).isCameraEntity()) {
            fpmSubmittingCameraEntity = true;
            FirstPersonModelCore.instance.setRenderingPlayer(true);
        }
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/level/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
            at = @At("TAIL"), expect = 0, require = 0)
    private void fpmSubmitEnd(EntityRenderState renderState, CameraRenderState camera, double x, double y, double z,
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (fpmSubmittingCameraEntity) {
            fpmSubmittingCameraEntity = false;
            FirstPersonModelCore.instance.setRenderingPlayer(false);
        }
    }
    //? } else if >= 1.21.3 {
    /*
    
    @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
    private static void renderHitbox(PoseStack poseStack, VertexConsumer buffer,
            //? if >= 1.21.5 {
    /^
            HitboxRenderState hitboxRenderState,
            ^///? } else {
    
             Entity entity, float red, float green, float blue, float alpha, 
            //? }
            CallbackInfo ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            ci.cancel();
        }
    }
    
    *///? } else {
    /*
    
    @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
    //? if < 1.17.0 {
    /^
       private void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f, CallbackInfo ci) {
     ^///? } else if < 1.21.0 {
    /^
    private static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f,
            CallbackInfo ci) {
        ^///? } else {
        
          private static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f,
            float g, float h, float i,
                    CallbackInfo ci) {
         //? }
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            ci.cancel();
        }
    }
    *///? }

}
