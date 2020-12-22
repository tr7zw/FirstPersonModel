package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.network.play.server.SEntityMetadataPacket;

//ClientPlayNetworkHandler
@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetHandlerMixin {

	@Shadow
	private Minecraft client;
	@Shadow
	private ClientWorld world;

	@Inject(method = "handleEntityMetadata", at = @At("HEAD"))
	public void handleEntityMetadata(SEntityMetadataPacket packet, CallbackInfo callback) {
		if (world != null) {
			Entity entity = world.getEntityByID(packet.getEntityId());
			if (entity != null && packet.getDataManagerEntries() != null && entity instanceof PlayerSettings) {
				for (DataEntry<?> entry : packet.getDataManagerEntries()) {
					if (entry.getKey().getId() == 16 && (Byte) entry.getValue() == 0) {
						FirstPersonModelMod.syncManager.updateSettings((PlayerSettings) entity);
					}
				}
			}
		}

	}
	
}
