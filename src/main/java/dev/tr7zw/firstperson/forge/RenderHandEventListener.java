//#if FORGE || NEOFORGE
//$$ package dev.tr7zw.firstperson.forge;
//$$ 
//$$ import dev.tr7zw.firstperson.FirstPersonModelCore;
//#if FORGE
//$$ import net.minecraftforge.client.event.RenderHandEvent;
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//#else
//$$ import net.neoforged.bus.api.SubscribeEvent;
//$$ import net.neoforged.neoforge.client.event.RenderHandEvent;
//#endif
//$$ 
//$$ public class RenderHandEventListener {
//$$ 
//$$ 	@SubscribeEvent
//$$ 	public void onRender(RenderHandEvent e) {
//$$ 		if(FirstPersonModelCore.instance.isEnabled() && !FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
//$$ 			e.setCanceled(true);
//$$ 		}
//$$ 	}
//$$ 	
//$$ }
//#endif