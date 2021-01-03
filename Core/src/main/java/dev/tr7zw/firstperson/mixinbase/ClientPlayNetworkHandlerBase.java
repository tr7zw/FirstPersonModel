package dev.tr7zw.firstperson.mixinbase;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.velvet.api.wrapper.WrappedEntity;
import dev.tr7zw.velvet.api.wrapper.WrappedEntityTrackerUpdate;
import dev.tr7zw.velvet.api.wrapper.WrappedWorld;

public interface ClientPlayNetworkHandlerBase {

	public default void handle(WrappedWorld world, WrappedEntityTrackerUpdate packet) {
		if (world != null) {
			WrappedEntity entity = world.getEntityById(packet.id());
			if (entity != null && packet.hasTrackedValues() && entity.getHandler() instanceof PlayerSettings) {
				packet.forEach((id, data) -> {
					if (id == 16 && (Byte) data == 0) {
						FirstPersonModelCore.syncManager.updateSettings((PlayerSettings) entity.getHandler());
					}
				});
			}
		}
	}
	
}
