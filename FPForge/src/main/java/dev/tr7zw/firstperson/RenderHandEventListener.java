package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderHandEventListener {

	@SubscribeEvent
	public void onRender(RenderHandEvent e) {
		if(FirstPersonModelCore.enabled && !FirstPersonModelCore.config.firstPerson.vanillaHands) {
			e.setCanceled(true);
		}
	}
	
}
