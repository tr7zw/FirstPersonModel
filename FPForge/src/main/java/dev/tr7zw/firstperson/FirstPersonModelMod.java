package dev.tr7zw.firstperson;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

@Mod(FirstPersonModelMod.MODID)
public class FirstPersonModelMod extends FirstPersonModelCore
{

    public static final String MODID = "firstpersonmod";

    public FirstPersonModelMod() {
        try {
            Class clientClass = net.minecraft.client.Minecraft.class;
        }catch(Throwable ex) {
            System.out.println("Firstperson Mod installed on a Server. Going to sleep.");
            return;
        }
    	instance = this;
        // Register the setup method for modloading
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.addListener(new RenderHandEventListener()::onRender);
        MinecraftForge.EVENT_BUS.addListener(this::doTick);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }
    
    private void doTick(ClientTickEvent event) {
    	super.onTick();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	sharedSetup();
    }

    @Override
    public void registerKeybinds() {
        ClientRegistry.registerKeyBinding(keyBinding);
    }

}
