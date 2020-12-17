package dev.tr7zw.firstperson.forge;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import dev.tr7zw.firstperson.forge.config.ForgeConfigBuilder;
import dev.tr7zw.firstperson.forge.listener.PlayerRendererListener;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FirstPersonModelMod.MODID)
public class FirstPersonModelMod extends FirstPersonModelCore
{

    public static final String MODID = "firstpersonmod";

    private static final Logger LOGGER = LogManager.getLogger();
    
	@Nullable
	public static MatrixStack hideHeadWithMatrixStack = null;
	@Nullable
	public static MatrixStack paperDollStack = null; //Make force compatibility not hide paper doll
    private MinecraftWrapper wrapper;

    public FirstPersonModelMod() {
    	instance = this;
        // Register the setup method for modloading
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.addListener(PlayerRendererListener::onRender);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> new ForgeConfigBuilder()::createConfigScreen
        );
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
	public boolean isFixActive(Object player, Object matrices){
		return Minecraft.getInstance() != null && Minecraft.getInstance().getRenderViewEntity() == player /*&& (matrices == hideHeadWithMatrixStack || config.firstPerson.forceActive && matrices != paperDollStack)*/;
	}
	
	

}
