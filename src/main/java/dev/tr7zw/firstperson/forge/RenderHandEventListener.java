//? if forge || neoforge {
/*
package dev.tr7zw.firstperson.forge;

import dev.tr7zw.firstperson.FirstPersonModelCore;

//? if >= 1.21.6 && forge {
/^
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
^///? } else if forge {
// import net.minecraftforge.client.event.RenderHandEvent;
// import net.minecraftforge.eventbus.api.SubscribeEvent;
//? }

//? if neoforge {
/^
  import net.neoforged.bus.api.SubscribeEvent;
  import net.neoforged.neoforge.client.event.RenderHandEvent;
 ^///? }

public class RenderHandEventListener {

    @SubscribeEvent
    //? if >= 1.21.6 && forge {
/^
    public boolean onRender(net.minecraftforge.client.event.RenderHandEvent e) {
        if (FirstPersonModelCore.instance.isEnabled()
                && !FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
            return true;
        }
        return false;
    }
    ^///? } else {
    
      	public void onRender(RenderHandEvent e) {
      		if(FirstPersonModelCore.instance.isEnabled() && !FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
      			e.setCanceled(true);
      		}
      	}
     //? }

}
*///? }
