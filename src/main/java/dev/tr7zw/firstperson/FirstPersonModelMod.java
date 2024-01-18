package dev.tr7zw.firstperson;

//spotless:off
//#if FABRIC
import net.fabricmc.api.ClientModInitializer;

public class FirstPersonModelMod extends FirstPersonModelCore implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		sharedSetup();
	}
//#else
//$$ import dev.tr7zw.util.ModLoaderUtil;
//$$ import dev.tr7zw.firstperson.forge.RenderHandEventListener;
//$$ public class FirstPersonModelMod extends FirstPersonModelCore {
//$$ public FirstPersonModelMod() {
//$$	ModLoaderUtil.registerForgeEvent(new RenderHandEventListener()::onRender);
//$$ }
//#endif
//spotless:on

}
