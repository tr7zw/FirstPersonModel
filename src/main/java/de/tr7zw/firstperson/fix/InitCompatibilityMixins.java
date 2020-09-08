package de.tr7zw.firstperson.fix;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class InitCompatibilityMixins implements Runnable {
    @Override
    public void run() {
        System.out.println("[FirstPersonModel] Init Compatibility Mixins");
        if(FabricLoader.getInstance().isModLoaded("canvas")) {
            System.out.println("[FirstPersonModel] Applying Canvas renderer Mixin");
            Mixins.addConfiguration("firstperson_canvas.mixin.json");
        }
    }
}

