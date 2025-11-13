package dev.tr7zw.firstperson.mixins;

//? if >= 1.21.9 {

import dev.tr7zw.firstperson.access.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.entity.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

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
        if (renderState instanceof AvatarRenderState avatarRenderState && avatarRenderState.isSpectator) {
            // Do not touch or change spectator avatars
            head.visible = true;
            return;
        }
        LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) renderState;
        head.visible = !access.isCameraEntity();
        leftArm.visible = !access.hideLeftArm();
        leftArm.xRot += access.getArmOffset();
        rightArm.visible = !access.hideRightArm();
        rightArm.xRot += access.getArmOffset();
        body.visible = !access.hideBody();
    }
}
//? } else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public class HumanoidModelMixin {
}
*///? }
