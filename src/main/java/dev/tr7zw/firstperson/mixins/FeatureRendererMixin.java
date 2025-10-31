package dev.tr7zw.firstperson.mixins;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
//? if >= 1.21.3 {

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//? }
//? if < 1.21.4 {

// import net.minecraft.client.model.VillagerHeadModel;
//? }

@Mixin(RenderLayer.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class FeatureRendererMixin {
    /*@Inject(method = "renderColoredCutoutModel", at = @At("HEAD"), cancellable = true)
    //? if >= 1.21.3 {
    
    private static void renderColoredCutoutModel(Model<? super LivingEntityRenderState> model, ResourceLocation resourceLocation, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, LivingEntityRenderState livingEntityRenderState, int j, int k, CallbackInfo ci) {
    //? } else if >= 1.21.0 {
    
        // private static <T extends LivingEntity> void removeHead(EntityModel model, ResourceLocation texture,
        //        PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, int color, CallbackInfo ci) {
    //? } else {
    
        // private static <T extends LivingEntity> void removeHead(EntityModel<T> model, ResourceLocation texture,
        //        PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float red, float green,
        //        float blue, CallbackInfo ci) {
    //? }
        if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            if (!(model instanceof HeadedModel)) {
                ci.cancel();
                return;
            }
            ((ModelPartBase) (Object) ((HeadedModel) model).getHead()).setHidden();
    //? if < 1.21.4 {
    
            // if (model instanceof VillagerHeadModel villager) {
            //    villager.hatVisible(false);
            // }
    //? }
        }
    }
    
    @Inject(method = "renderColoredCutoutModel", at = @At("RETURN"), cancellable = true)
    //? if >= 1.21.3 {
    
    private static void removeReturn(EntityModel<?> model, ResourceLocation resourceLocation, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, LivingEntityRenderState livingEntityRenderState, int j,
            CallbackInfo ci) {
    //? } else if >= 1.21.0 {
    
        // private static <T extends LivingEntity> void removeReturn(EntityModel model, ResourceLocation texture,
        //        PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, int color, CallbackInfo ci) {
    //? } else {
    
        // private static <T extends LivingEntity> void removeReturn(EntityModel<T> model, ResourceLocation texture,
        //        PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float red, float green,
        //        float blue, CallbackInfo ci) {
    //? }
        if (model instanceof HeadedModel) {
            ((ModelPartBase) (Object) ((HeadedModel) model).getHead()).showAgain();
    //? if < 1.21.4 {
    
            //  if (model instanceof VillagerHeadModel villager) {
            //      villager.hatVisible(true);
            //  }
    //? }
        }
    }*/

}
