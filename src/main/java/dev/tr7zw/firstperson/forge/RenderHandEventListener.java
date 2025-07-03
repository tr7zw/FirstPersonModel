//#if FORGE || NEOFORGE
//$$ package dev.tr7zw.firstperson.forge;
//$$ 
//$$ import dev.tr7zw.firstperson.FirstPersonModelCore;
//#if FORGE
//$$ import net.minecraftforge.client.event.RenderHandEvent;
//#if MC >= 12106
//$$ import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
//#else
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//#endif
//#else
//$$ import net.neoforged.bus.api.SubscribeEvent;
//$$ import net.neoforged.neoforge.client.event.RenderHandEvent;
//#endif
//$$ 
//$$ public class RenderHandEventListener {
//$$ 
//$$ 	@SubscribeEvent
//#if MC >= 12106 && FORGE
//$$    public boolean onRender(RenderHandEvent e) {
//$$            System.out.println("Render Hand Event");
//$$                if(FirstPersonModelCore.instance.isEnabled() && !FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
//$$                        return true;
//$$                }
//$$                return false;
//$$        }
//#else
//$$ 	public void onRender(RenderHandEvent e) {
//$$ 		if(FirstPersonModelCore.instance.isEnabled() && !FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
//$$ 			e.setCanceled(true);
//$$ 		}
//$$ 	}
//#endif
//$$ 	
//$$ }
//#endif