package dev.tr7zw.firstperson.config;

import java.util.ArrayList;
import java.util.List;

import dev.tr7zw.config.CustomConfigScreen;
import dev.tr7zw.firstperson.FirstPersonModelMod;
import dev.tr7zw.firstperson.versionless.config.FirstPersonSettings;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;

public class ConfigScreenProvider {

    public static Screen createConfigScreen(Screen parent) {
        CustomConfigScreen screen = new CustomConfigScreen(parent, "text.firstperson.title") {

            @Override
            public void initialize() {
                getOptions().addBig(getOnOffOption("text.firstperson.option.firstperson.enabledByDefault",
                        () -> FirstPersonModelMod.config.enabledByDefault,
                        (b) -> FirstPersonModelMod.config.enabledByDefault = b));

                List<Object> options = new ArrayList<>();
                options.add(getIntOption("text.firstperson.option.firstperson.xOffset", -40, 40,
                        () -> FirstPersonModelMod.config.xOffset, (i) -> FirstPersonModelMod.config.xOffset = i));
                options.add(getIntOption("text.firstperson.option.firstperson.sneakXOffset", -40, 40,
                        () -> FirstPersonModelMod.config.sneakXOffset,
                        (i) -> FirstPersonModelMod.config.sneakXOffset = i));
                options.add(getIntOption("text.firstperson.option.firstperson.sitXOffset", -40, 40,
                        () -> FirstPersonModelMod.config.sitXOffset, (i) -> FirstPersonModelMod.config.sitXOffset = i));
                options.add(getOnOffOption("text.firstperson.option.firstperson.renderStuckFeatures",
                        () -> FirstPersonModelMod.config.renderStuckFeatures,
                        (b) -> FirstPersonModelMod.config.renderStuckFeatures = b));
                options.add(getOnOffOption("text.firstperson.option.firstperson.vanillaHands",
                        () -> FirstPersonModelMod.config.vanillaHands,
                        (b) -> FirstPersonModelMod.config.vanillaHands = b));
                options.add(getOnOffOption("text.firstperson.option.firstperson.doubleHands",
                        () -> FirstPersonModelMod.config.doubleHands,
                        (b) -> FirstPersonModelMod.config.doubleHands = b));

                getOptions().addSmall(options.toArray(new OptionInstance[0]));

            }

            @Override
            public void save() {
                FirstPersonModelMod.instance.writeSettings();
            }

            @Override
            public void reset() {
                FirstPersonModelMod.config = new FirstPersonSettings();
                FirstPersonModelMod.instance.writeSettings();
            }

        };

        return screen;
    }

}
