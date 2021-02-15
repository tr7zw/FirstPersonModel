package dev.tr7zw.firstperson.mixinbase;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.transliterationlib.api.wrapper.WrappedEntityTrackerUpdate;
import dev.tr7zw.transliterationlib.api.wrapper.entity.Entity;
import dev.tr7zw.transliterationlib.api.wrapper.world.World;


public interface ClientPlayNetworkHandlerBase {

	public default void handle(World world, WrappedEntityTrackerUpdate packet) {
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
