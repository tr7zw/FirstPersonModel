package dev.tr7zw.firstperson;

import com.mojang.blaze3d.platform.*;
import dev.tr7zw.transition.loader.*;
import dev.tr7zw.transition.mc.*;
import lombok.*;
import net.minecraft.client.*;

public final class KeybindHolder {
    public static final KeybindHolder INSTANCE = new KeybindHolder();
    private boolean initialized = false;

    //? if >= 1.20.1 {
    private static final int TOGGLE_KEY = InputConstants.KEY_F6;
    //? } else {
    /*
    private static final int TOGGLE_KEY = 295;
     */
    //? }

    @Getter
    private KeyMapping keyBinding = GeneralUtil.createKeyMapping("key.firstperson.toggle", TOGGLE_KEY,
            "firstperson:keybind");

    private KeybindHolder() {
    }

    public void registerKeybinds() {
        if (initialized)
            return;
        initialized = true;
        ModLoaderUtil.registerKeybind(keyBinding);
    }

}
