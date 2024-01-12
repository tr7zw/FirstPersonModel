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
//$$ public class FirstPersonModelMod extends FirstPersonModelCore {
//$$ public FirstPersonModelMod() {
//$$ 	ModLoaderUtil.registerClientSetupListener(this::sharedSetup);
//$$ }
//#endif
//spotless:on

}
