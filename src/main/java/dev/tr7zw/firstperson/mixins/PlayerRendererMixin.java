package dev.tr7zw.firstperson.mixins;

import java.util.ArrayList;
import java.util.List;

import dev.tr7zw.firstperson.InventoryUtil;
import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import dev.tr7zw.transition.mc.EntityUtil;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.PlayerRendererAccess;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.api.PlayerOffsetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
//import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;
//? if >= 1.18.2 {

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
//? }
//? if >= 1.21.3 {

//import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//? }

/**
 * Offset the player behind the camera
 * 
 * @author tr7zw
 *
 */
@Mixin(value = AvatarRenderer.class, priority = 500)
public abstract class PlayerRendererMixin extends LivingEntityRenderer implements PlayerRendererAccess {

//? if >= 1.18.2 {

    public PlayerRendererMixin(Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }
//? } else {

    // public PlayerRendererMixin(EntityRenderDispatcher entityRenderDispatcher, EntityModel entityModel, float f) {
    //    super(entityRenderDispatcher, entityModel, f);
    // }
//? }

    private static Minecraft fpmMcInstance = Minecraft.getInstance();
    private List<RenderLayer> removedLayers = new ArrayList<>();

    @Inject(method = "getRenderOffset", at = @At("RETURN"), cancellable = true)
//? if >= 1.21.3 {

    public void getRenderOffset(AvatarRenderState playerRenderState, CallbackInfoReturnable<Vec3> ci) {
        AbstractClientPlayer entity = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
//? } else {

        // public void getRenderOffset(AbstractClientPlayer entity, float delta, CallbackInfoReturnable<Vec3> ci) {
//? }
        LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) playerRenderState;
        if (access.isCameraEntity()) {
            Vec3 offset = ci.getReturnValue().add(access.getRenderOffset());
            // FIXME use render state instead of entity
            for (PlayerOffsetHandler handler : FirstPersonAPI.getPlayerOffsetHandlers()) {
                offset = handler.applyOffset(entity, delta, ci.getReturnValue(), offset);
            }

            ci.setReturnValue(offset);
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    public void extractRenderState(Avatar avatar, AvatarRenderState avatarRenderState, float delta, CallbackInfo ci) {
        LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) avatarRenderState;
        if (!access.isCameraEntity()) return;
        if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(avatar)) {
            access.setHideArms(true);
        } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {
            access.setArmOffset(Mth.clamp(-EntityUtil.getXRot(avatar) / 20 + 2, -0.0f, 0.7f));
            if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown(avatar)) {// TODO DYNAMIC HAND
                if (!avatar.getOffhandItem().isEmpty()
                        || avatar.getMainHandItem().getItem().equals(Items.FILLED_MAP)) {
                    access.setHideLeftArm(true);
                }
                if (!avatar.getMainHandItem().isEmpty()) {
                    access.setHideRightArm(true);
                }
            }
        }
        if (avatar.isSwimming()) {
            access.setHideBody(true);
            avatarRenderState.showCape = false;
        }
        if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(avatar, avatar.getMainHandItem(), avatar.getOffhandItem())) access.setHideArms(true);
        FirstPersonModelCore.instance.getLogicHandler().updatePositionOffset(avatar, delta);
        access.setRenderOffset(FirstPersonModelCore.instance.getLogicHandler().getOffset());
    }

    @Override
    public List<RenderLayer> getRenderLayers() {
        List<RenderLayer> layers = new ArrayList<>(this.layers);
        layers.addAll(removedLayers);
        return layers;
    }

    @Override
    public void updatePartsList(boolean thirdperson) {
        this.layers.addAll(removedLayers);
        removedLayers.clear();
        if (thirdperson)
            return;
        for (Object layerObj : this.layers) {
            RenderLayer layer = (RenderLayer) layerObj; // Workaround for 1.16 generic issues
            if (FirstPersonModelCore.instance.getConfig().hiddenLayers.contains(layer.getClass().getName())) {
                removedLayers.add(layer);
            }
        }
        this.layers.removeAll(removedLayers);
    }

}
