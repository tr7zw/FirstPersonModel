package dev.tr7zw.firstperson.mixins;

//? if < 1.21.9 {
/*import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.versionless.mixinbase.*;
import lombok.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

//? if < 1.21.4 {

// import net.minecraft.client.model.VillagerHeadModel;
//? }

@Mixin(RenderLayer.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class FeatureRendererMixin {
    @Inject(method = "renderColoredCutoutModel", at = @At("HEAD"), cancellable = true)
    //? if >= 1.21.3 {

    private static void renderColoredCutoutModel(EntityModel<?> model, ResourceLocation resourceLocation,
            PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            LivingEntityRenderState livingEntityRenderState, int j, CallbackInfo ci) {
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
    }
}
*///? } else {
@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public class FeatureRendererMixin {
}
//? }
