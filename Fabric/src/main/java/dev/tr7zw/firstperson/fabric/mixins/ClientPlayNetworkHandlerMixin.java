package dev.tr7zw.firstperson.fabric.mixins;

import static dev.tr7zw.velvet.api.Velvet.velvet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.mixinbase.ClientPlayNetworkHandlerBase;
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
		handle(world != null ? velvet.getWrapper().wrapWorld(world) : null, velvet.getWrapper().wrapEntityTrackerUpdatePacket(packet));
	}

}
