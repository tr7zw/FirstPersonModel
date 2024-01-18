package dev.tr7zw.firstperson;

import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderHandEventListener {

    @SubscribeEvent
    public void onRender(RenderHandEvent e) {
        if (FirstPersonModelCore.enabled && !FirstPersonModelCore.instance.showVanillaHands()) {
            e.setCanceled(true);
        }
    }

}
