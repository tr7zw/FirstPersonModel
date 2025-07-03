package dev.tr7zw.firstperson;

//#if FABRIC
import net.fabricmc.api.ClientModInitializer;
//#endif

public class FirstPersonModelMod extends FirstPersonModelCore
        //#if FABRIC
        implements ClientModInitializer
//#endif
{

    //#if FABRIC
    @Override
    //#endif
    public void onInitializeClient() {
        sharedSetup();
    }

}
