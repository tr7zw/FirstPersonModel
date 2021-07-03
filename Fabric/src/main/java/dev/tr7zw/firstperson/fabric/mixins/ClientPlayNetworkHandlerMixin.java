package dev.tr7zw.firstperson.fabric.mixins;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.mixinbase.ClientPlayNetworkHandlerBase;
import dev.tr7zw.transliterationlib.api.wrapper.WrappedEntityTrackerUpdate;
import dev.tr7zw.transliterationlib.api.wrapper.entity.Entity;
import dev.tr7zw.transliterationlib.api.wrapper.world.World;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;

/**
 * Catches the player layer update packet and notifies the syncManager to update
 * this players settings
 *
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerBase {

	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntityTrackerUpdate", at = @At("HEAD"))
	public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo callback) {
	    try {
		handle2(world != null ? transliteration.singletonWrapper().getWorld().of(world) : null, transliteration.getWrapper().wrapEntityTrackerUpdatePacket(packet));
	    }catch(Throwable t) {
	        t.printStackTrace();
	    }
	}
	
	   public void handle2(World world, WrappedEntityTrackerUpdate packet) {
	        if (world != null) {
	            Entity entity = world.getEntityById(packet.id());
	            if (entity != null && packet.hasTrackedValues() && entity.getHandler() instanceof PlayerSettings) {
	                packet.forEach((id, data) -> {
	                    if (id == 16 && data instanceof Byte &&  (Byte) data == 0) {
	                        FirstPersonModelCore.syncManager.updateSettings((PlayerSettings) entity.getHandler());
	                    }
	                });
	            }
	        }
	    }

}
