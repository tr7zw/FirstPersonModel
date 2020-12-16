package dev.tr7zw.firstperson.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FirstPersonModelMod.MODID)
public class FirstPersonModelMod extends FirstPersonModelCore
{

    public static final String MODID = "firstpersonmod";

    private static final Logger LOGGER = LogManager.getLogger();
    
    private MinecraftWrapper wrapper;

    public FirstPersonModelMod() {
    	instance = this;
        // Register the setup method for modloading
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	wrapper = new ForgeWrapper(Minecraft.getInstance());
    	sharedSetup();
    }

	@Override
	public MinecraftWrapper getWrapper() {
		return wrapper;
	}

	@Override
	public boolean isFixActive(Object player, Object matrices) {
		// TODO Auto-generated method stub
		return true;
	}

}
