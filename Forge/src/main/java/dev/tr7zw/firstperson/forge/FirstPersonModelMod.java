package dev.tr7zw.firstperson.forge;

import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.config.ConfigBuilder;
import dev.tr7zw.firstperson.forge.listener.PlayerRendererListener;
import dev.tr7zw.valvet.forge.ValvetImpl;
import dev.tr7zw.velvet.api.Velvet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FirstPersonModelMod.MODID)
public class FirstPersonModelMod extends FirstPersonModelCore
{

    public static final String MODID = "firstpersonmod";
    
	@Nullable
	public static MatrixStack hideHeadWithMatrixStack = null;
	@Nullable
	public static MatrixStack paperDollStack = null; //Make force compatibility not hide paper doll

    public FirstPersonModelMod() {
    	instance = this;
        // Register the setup method for modloading
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.addListener(this::doTick);
        MinecraftForge.EVENT_BUS.addListener(PlayerRendererListener::onRender);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> new BiFunction<Minecraft, Screen, Screen>() {
					@Override
					public Screen apply(Minecraft t, Screen screen) {
						return new ConfigBuilder().createConfigScreen(Velvet.velvet.getWrapper().wrapScreen(screen)).getHandler(Screen.class);
					}
				}
        );
    }
    
    private void doTick(ClientTickEvent event) {
    	super.onTick();
    }
    

    private void doClientStuff(final FMLClientSetupEvent event) {
    	wrapper = new ForgeWrapper(Minecraft.getInstance());
    	Velvet.velvet = new ValvetImpl();
    	sharedSetup();
    }

	@Override
	public boolean isFixActive(Object player, Object matrices){
		return Minecraft.getInstance() != null && Minecraft.getInstance().getRenderViewEntity() == player && (matrices == hideHeadWithMatrixStack || config.firstPerson.forceActive && matrices != paperDollStack);
	}
	
	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (enabled && (config.firstPerson.forceActive || hideHeadWithMatrixStack == matrixStack));
	}
	

}
