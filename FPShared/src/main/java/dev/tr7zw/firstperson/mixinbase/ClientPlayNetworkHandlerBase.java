package dev.tr7zw.firstperson.mixinbase;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;


public interface ClientPlayNetworkHandlerBase {

	public default void handle(Level world, ClientboundSetEntityDataPacket packet) {
		if (world != null) {
			Entity entity = world.getEntity(packet.getId());
			if (entity != null && packet.getUnpackedData() != null && entity instanceof PlayerSettings) {
				packet.getUnpackedData().forEach((entry) -> {
					
				    if (entry.getAccessor().getId() == 16 && entry.getValue() instanceof Byte &&  (Byte) entry.getValue() == 0) {
						FirstPersonModelCore.syncManager.updateSettings((PlayerSettings) entity);
					}
				});
			}
		}
	}
	
}
