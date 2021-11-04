package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.mixinbase.ClientPlayNetworkHandlerBase;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;

/**
 * Catches the player layer update packet and notifies the syncManager to update
 * this players settings
 *
 */
@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerBase {

	@Shadow
	private ClientLevel level;

	@Inject(method = "handleSetEntityData", at = @At("HEAD"))
	public void onEntityTrackerUpdate(ClientboundSetEntityDataPacket packet, CallbackInfo callback) {
	    try {
		handle(level, packet);
	    }catch(Throwable t) {
	        t.printStackTrace();
	    }
	}

}
