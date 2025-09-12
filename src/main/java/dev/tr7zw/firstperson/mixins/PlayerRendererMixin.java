package dev.tr7zw.firstperson.mixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.PlayerRendererAccess;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.api.PlayerOffsetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;
//#if MC >= 11802
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
//#endif
//#if MC >= 12103
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif

/**
 * Offset the player behind the camera
 * 
 * @author tr7zw
 *
 */
@Mixin(value = PlayerRenderer.class, priority = 500)
public abstract class PlayerRendererMixin extends LivingEntityRenderer implements PlayerRendererAccess {

    //#if MC >= 11802
    public PlayerRendererMixin(Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }
    //#else
    //$$public PlayerRendererMixin(EntityRenderDispatcher entityRenderDispatcher, EntityModel entityModel, float f) {
    //$$    super(entityRenderDispatcher, entityModel, f);
    //$$}
    //#endif

    private static Minecraft fpmMcInstance = Minecraft.getInstance();
    private List<RenderLayer> removedLayers = new ArrayList<>();

    @Inject(method = "getRenderOffset", at = @At("RETURN"), cancellable = true)
    //#if MC >= 12103
    public void getRenderOffset(PlayerRenderState playerRenderState, CallbackInfoReturnable<Vec3> ci) {
        AbstractClientPlayer entity = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        //#else
        //$$public void getRenderOffset(AbstractClientPlayer entity, float delta, CallbackInfoReturnable<Vec3> ci) {
        //#endif
        if (entity == fpmMcInstance.cameraEntity && FirstPersonModelCore.instance.isRenderingPlayer()) {
            FirstPersonModelCore.instance.getLogicHandler().updatePositionOffset(entity, delta);

            Vec3 offset = ci.getReturnValue().add(FirstPersonModelCore.instance.getLogicHandler().getOffset());
            for (PlayerOffsetHandler handler : FirstPersonAPI.getPlayerOffsetHandlers()) {
                offset = handler.applyOffset(entity, delta, ci.getReturnValue(), offset);
            }

            ci.setReturnValue(offset);
        }
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
