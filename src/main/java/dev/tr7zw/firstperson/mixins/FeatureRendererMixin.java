package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

@Mixin(RenderLayer.class)
public abstract class FeatureRendererMixin {
    @Inject(method = "renderColoredCutoutModel", at = @At("HEAD"), cancellable = true)
    private static <T extends LivingEntity> void removeHead(EntityModel<T> model, ResourceLocation texture,
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float red, float green,
            float blue, CallbackInfo ci) {
        if (FirstPersonModelCore.isRenderingPlayer) {
            if (!(model instanceof HeadedModel)) {
                ci.cancel();
                return;
            }
            ((ModelPartBase) (Object) ((HeadedModel) model).getHead()).setHidden();
            if (model instanceof VillagerHeadModel) {
                ((VillagerHeadModel) model).hatVisible(false);
            }
        }
    }

    @Inject(method = "renderColoredCutoutModel", at = @At("RETURN"), cancellable = true)
    private static <T extends LivingEntity> void removeReturn(EntityModel<T> model, ResourceLocation texture,
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float red, float green,
            float blue, CallbackInfo ci) {
        if (model instanceof HeadedModel) {
            ((ModelPartBase) (Object) ((HeadedModel) model).getHead()).showAgain();
            if (model instanceof VillagerHeadModel) {
                ((VillagerHeadModel) model).hatVisible(true);
            }
        }
    }

}
