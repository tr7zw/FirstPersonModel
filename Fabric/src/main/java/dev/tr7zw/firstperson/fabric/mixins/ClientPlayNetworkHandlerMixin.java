package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker.Entry;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;

/**
 * Catches the player layer update packet and notifies the syncManager to update
 * this players settings
 *
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow
	private MinecraftClient client;
	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntityTrackerUpdate", at = @At("HEAD"))
	public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo callback) {
		if (world != null) {
			Entity entity = world.getEntityById(packet.id());
			if (entity != null && packet.getTrackedValues() != null && entity instanceof PlayerSettings) {
				for (Entry<?> entry : packet.getTrackedValues()) {
					if (entry.getData().getId() == 16 && (Byte) entry.get() == 0) {
						FirstPersonModelMod.syncManager.updateSettings((PlayerSettings) entity);
					}
				}
			}
		}

	}

}
