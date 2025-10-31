package dev.tr7zw.firstperson;

//? if fabric {

import net.fabricmc.api.ClientModInitializer;
//? }

public class FirstPersonModelMod extends FirstPersonModelCore
        //? if fabric {

        implements ClientModInitializer
//? }
{

    //? if fabric {

    @Override
    //? }
    public void onInitializeClient() {
        sharedSetup();
    }

}
