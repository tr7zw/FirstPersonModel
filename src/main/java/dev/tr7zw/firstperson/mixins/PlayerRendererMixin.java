package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.access.*;
import dev.tr7zw.transition.mc.*;
import net.minecraft.client.*;
//? if >=1.21.11 {
import net.minecraft.client.model.player.*;
//?} else {
/*import net.minecraft.client.model.*;
*///?}
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.player.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

/**
 * Offset the player behind the camera
 * 
 * @author tr7zw
 *
 */
//? if >= 1.21.9 {
@Mixin(value = AvatarRenderer.class, priority = 500)
//? } else {
/*@Mixin(value = PlayerRenderer.class, priority = 500)
*///? }
public abstract class PlayerRendererMixin extends LivingEntityRenderer implements PlayerRendererAccess {

    //? if >= 1.18.2 {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }
    //? } else {
    /*
     public PlayerRendererMixin(EntityRenderDispatcher entityRenderDispatcher, EntityModel entityModel, float f) {
        super(entityRenderDispatcher, entityModel, f);
     }
    *///? }

    private static Minecraft fpmMcInstance = Minecraft.getInstance();
    private List<RenderLayer> removedLayers = new ArrayList<>();

    //? if >= 1.21.9 {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    public void extractRenderState(Avatar avatar, AvatarRenderState avatarRenderState, float delta, CallbackInfo ci) {
        LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) avatarRenderState;
        if (!access.isCameraEntity())
            return;
        if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(avatar)) {
            access.setHideArms(true);
        } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {
            access.setArmOffset(Mth.clamp(-EntityUtil.getXRot(avatar) / 20 + 2, -0.0f, 0.7f));
            if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown(avatar)) {// TODO DYNAMIC HAND
                if (!avatar.getOffhandItem().isEmpty() || avatar.getMainHandItem().getItem().equals(Items.FILLED_MAP)) {
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
        if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(avatar, avatar.getMainHandItem(),
                avatar.getOffhandItem()))
            access.setHideArms(true);
        //? if <1.21.11
        /*avatarRenderState.hitboxesRenderState = null;*/
    }
    //? }

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
