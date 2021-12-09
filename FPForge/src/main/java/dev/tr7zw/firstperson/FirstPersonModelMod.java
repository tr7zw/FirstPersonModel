package dev.tr7zw.firstperson;

import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString(),
                        (remote, isServer) -> true));
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, () -> new ConfigGuiFactory((mc, screen) -> {
            return createConfigScreen(screen);
        }));

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
