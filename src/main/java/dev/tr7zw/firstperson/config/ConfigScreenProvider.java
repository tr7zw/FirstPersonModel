package dev.tr7zw.firstperson.config;

import java.util.ArrayList;
import java.util.List;

import dev.tr7zw.config.CustomConfigScreen;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.versionless.config.VanillaHands;
import lombok.experimental.UtilityClass;
//spotless:off 
//#if MC >= 11900
import net.minecraft.client.OptionInstance;
//#else
//$$ import net.minecraft.client.Option;
//#endif
//spotless:on
import net.minecraft.client.gui.screens.Screen;

@UtilityClass
public class ConfigScreenProvider {

    public static Screen createConfigScreen(Screen parent) {
        return new CustomConfigScreen(parent, "text.firstperson.title") {

            @Override
            public void initialize() {
                FirstPersonModelCore fpm = FirstPersonModelCore.instance;
                getOptions().addBig(getOnOffOption("text.firstperson.option.firstperson.enabledByDefault",
                        () -> fpm.getConfig().enabledByDefault, b -> fpm.getConfig().enabledByDefault = b));

                List<Object> options = new ArrayList<>();
                options.add(getIntOption("text.firstperson.option.firstperson.xOffset", -40, 40,
                        () -> fpm.getConfig().xOffset, i -> fpm.getConfig().xOffset = i));
                options.add(getIntOption("text.firstperson.option.firstperson.sneakXOffset", -40, 40,
                        () -> fpm.getConfig().sneakXOffset, i -> fpm.getConfig().sneakXOffset = i));
                options.add(getIntOption("text.firstperson.option.firstperson.sitXOffset", -40, 40,
                        () -> fpm.getConfig().sitXOffset, i -> fpm.getConfig().sitXOffset = i));
                options.add(getOnOffOption("text.firstperson.option.firstperson.renderStuckFeatures",
                        () -> fpm.getConfig().renderStuckFeatures, b -> fpm.getConfig().renderStuckFeatures = b));
                options.add(getEnumOption("text.firstperson.option.firstperson.vanillaHandMode", VanillaHands.class,
                        () -> fpm.getConfig().vanillaHandsMode, b -> fpm.getConfig().vanillaHandsMode = b));
                options.add(getOnOffOption("text.firstperson.option.firstperson.dynamicMode",
                        () -> fpm.getConfig().dynamicMode, b -> fpm.getConfig().dynamicMode = b));

                // spotless:off
                //#if MC >= 11900
                getOptions().addSmall(options.toArray(new OptionInstance[0]));
                //#else
                //$$getOptions().addSmall(options.toArray(new Option[0]));
                //#endif
                // spotless:on

            }

            @Override
            public void save() {
                FirstPersonModelCore.instance.writeSettings();
            }

            @Override
            public void reset() {
                FirstPersonModelCore.instance.resetSettings();
                FirstPersonModelCore.instance.writeSettings();
            }

        };

    }

}
