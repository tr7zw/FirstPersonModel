package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart body;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void setupAnim(HumanoidRenderState renderState, CallbackInfo ci) {
        LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) renderState;
        head.visible = !access.isCameraEntity();
        leftArm.visible = !access.hideLeftArm();
        leftArm.xRot += access.getArmOffset();
        rightArm.visible = !access.hideRightArm();
        rightArm.xRot += access.getArmOffset();
        body.visible = !access.hideBody();
    }
}
