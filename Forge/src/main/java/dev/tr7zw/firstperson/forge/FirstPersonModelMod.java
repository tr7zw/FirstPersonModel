package dev.tr7zw.firstperson.forge;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.config.ConfigBuilder;
import dev.tr7zw.firstperson.forge.listener.PaperDoll;
import dev.tr7zw.firstperson.forge.listener.RenderHandEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(FirstPersonModelMod.MODID)
public class FirstPersonModelMod extends FirstPersonModelCore
{

    public static final String MODID = "firstpersonmod";

    public FirstPersonModelMod() {
        try {
            Class clientClass = net.minecraft.client.MinecraftGame.class;
        }catch(Throwable ex) {
            System.out.println("Firstperson Mod installed on a Server. Going to sleep.");
            return;
        }
    	instance = this;
        // Register the setup method for modloading
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.addListener(new PaperDoll()::onOverlay);
        MinecraftForge.EVENT_BUS.addListener(new RenderHandEventListener()::onRender);
        MinecraftForge.EVENT_BUS.addListener(this::doTick);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> new BiFunction<Minecraft, Screen, Screen>() {
					@Override
					public Screen apply(Minecraft t, Screen screen) {
						return new ConfigBuilder().createConfigScreen(transliteration.getWrapper().wrapScreen(screen)).getHandler(Screen.class);
					}
				}
        );
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
    }
    
    private void doTick(ClientTickEvent event) {
    	super.onTick();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	wrapper = new ForgeWrapper(Minecraft.getInstance());
    	sharedSetup();
    }

	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (enabled && (config.firstPerson.forceActive || FirstPersonModelCore.isRenderingPlayer));
	}

}
