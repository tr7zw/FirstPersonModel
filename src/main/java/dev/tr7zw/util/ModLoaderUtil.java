package dev.tr7zw.util;

import java.util.function.Function;

import lombok.experimental.UtilityClass;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
//spotless:off
//#if FABRIC
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
//#elseif FORGE
//$$ import net.minecraft.client.Minecraft;
//$$ import org.apache.commons.lang3.ArrayUtils;
//$$ import net.minecraftforge.common.MinecraftForge;
//$$ import net.minecraftforge.event.TickEvent.ClientTickEvent;
//$$ import java.util.function.Consumer;
//$$ import net.minecraftforge.fml.ModLoadingContext;
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//$$ import net.minecraftforge.eventbus.api.Event;
//#if MC <= 11605
//$$ import net.minecraftforge.fml.ExtensionPoint;
//$$ import net.minecraftforge.fml.network.FMLNetworkConstants;
//$$ import org.apache.commons.lang3.tuple.Pair;
//#elseif MC <= 11701
//$$ import net.minecraftforge.fml.IExtensionPoint;
//$$ import net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory;
//#elseif MC <= 11802
//$$ import net.minecraftforge.fml.IExtensionPoint;
//$$ import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
//#else
//$$ import net.minecraftforge.fml.IExtensionPoint;
//$$ import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
//#endif
//#elseif NEOFORGE
//$$ import net.minecraft.client.Minecraft;
//$$ import org.apache.commons.lang3.ArrayUtils;
//$$ import java.util.function.Consumer;
//$$ import net.neoforged.fml.ModLoadingContext;
//$$ import net.neoforged.fml.IExtensionPoint;
//$$ import net.neoforged.bus.api.Event;
//$$ import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
//$$ import net.neoforged.neoforge.common.NeoForge;
//$$ import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//$$ import net.neoforged.neoforge.event.TickEvent.ClientTickEvent;
//$$ import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;
//#endif
//spotless:on

@UtilityClass
public class ModLoaderUtil {

    public static void registerKeybind(KeyMapping keyBinding) {
        // spotless:off
    	//#if FABRIC
    	KeyBindingHelper.registerKeyBinding(keyBinding);
    	//#elseif FORGE || NEOFORGE
    	//$$ Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyBinding);
    	//#endif
    	//spotless:on

    }

    public static void registerClientTickListener(Runnable runnable) {
        // spotless:off
    	//#if FABRIC
        ClientTickEvents.END_CLIENT_TICK.register(e -> {
            runnable.run();
        });
    	//#elseif FORGE
      //$$ MinecraftForge.EVENT_BUS.addListener(new Consumer<ClientTickEvent >() {
      //$$ 
      //$$ 	@Override
      //$$ 	public void accept(ClientTickEvent t) {
      //$$ 		runnable.run();
      //$$ 	}
      //$$ 	
      //$$ });
    	//#elseif NEOFORGE
      //$$  NeoForge.EVENT_BUS.addListener(new Consumer<ClientTickEvent>() {
      //$$     
      //$$    	@Override
      //$$    	public void accept(ClientTickEvent t) {
      //$$    		runnable.run();
      //$$     	}
      //$$     	
      //$$    });
    	//#endif
    	//spotless:on
    }

    public static boolean isModLoaded(String name) {
        // spotless:off
    	//#if FABRIC
        return FabricLoader.getInstance().isModLoaded(name);
        //#else
        //$$ return false;
    	//#endif
    	//spotless:on
    }

    public static void disableDisplayTest() {
        // spotless:off
    	//#if FORGE || NEOFORGE
    	//#if MC <= 11605
    	//$$ ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
    	//$$ () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
    	//#else
    	//$$        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
    	//$$                () -> new IExtensionPoint.DisplayTest(
    	//$$                       () -> ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString(),
    	//$$                        (remote, isServer) -> true));
    	//#endif
    	//#endif
    	//spotless:on
    }

    public static void registerConfigScreen(Function<Screen, Screen> createScreen) {
        // spotless:off
    	//#if FORGE || NEOFORGE
    	//#if MC <= 11605
    	//$$         ModLoadingContext.get().registerExtensionPoint(
    	//$$ ExtensionPoint.CONFIGGUIFACTORY,
    	//$$ () -> (mc, screen) -> createScreen.apply(screen));
    	//#elseif MC <= 11802
    	//$$ ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, ()
    	//$$ -> new ConfigGuiFactory((mc, screen) -> {
    	//$$            return createScreen.apply(screen);
    	//$$        }));
    	//#else
    	//$$ ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((mc, screen) -> {
    	//$$            return createScreen.apply(screen);
    	//$$        }));
    	//#endif 
    	//#endif
    	//spotless:on
    }

    public static void registerClientSetupListener(Runnable runnable) {
        // spotless:off
    	//#if FORGE || NEOFORGE
      //$$ FMLJavaModLoadingContext.get().getModEventBus().addListener(new Consumer<FMLClientSetupEvent>() {
      //$$ 
      //$$ 	@Override
      //$$ 	public void accept(FMLClientSetupEvent t) {
      //$$ 		runnable.run();
      //$$ 	}
      //$$ 	
      //$$ });
    	//#endif
    	//spotless:on
    }

    // spotless:off
	//#if FORGE
  //$$     public static <T extends Event> void registerForgeEvent(Consumer<T> handler) {
    	//$$     	MinecraftForge.EVENT_BUS.addListener(handler);
  //$$     }
	//#elseif NEOFORGE
    //$$    public static <T extends Event> void registerForgeEvent(Consumer<T> handler) {
    //$$    	NeoForge.EVENT_BUS.addListener(handler);
    //$$    }
	//#endif
	//spotless:on

}
