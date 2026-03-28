package dev.tr7zw.firstperson.config;

import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.access.*;
import dev.tr7zw.firstperson.versionless.config.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.widget.*;
import dev.tr7zw.trender.gui.widget.data.*;
import dev.tr7zw.trender.gui.widget.icon.*;
import lombok.experimental.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.renderer.entity.layers.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.Map.*;

@UtilityClass
public class ConfigScreenProvider {

    public static Screen createConfigScreen(Screen parent) {
        return new CustomConfigScreen(parent).createScreen();
    }

    private static class CustomConfigScreen extends AbstractConfigScreen {

        public CustomConfigScreen(Screen previous) {
            super(ComponentProvider.translatable("text.firstperson.title"), previous);

            WGridPanel root = new WGridPanel(8);
            root.setInsets(Insets.ROOT_PANEL);
            setRootPanel(root);

            WTabPanel wTabPanel = new WTabPanel();

            // options page
            List<OptionInstance> options = new ArrayList<>();

            options.add(getIntOption("text.firstperson.option.firstperson.xOffset", -40, 40,
                    () -> FirstPersonModelCore.instance.getConfig().xOffset,
                    i -> FirstPersonModelCore.instance.getConfig().xOffset = i));
            options.add(getIntOption("text.firstperson.option.firstperson.sneakXOffset", -40, 40,
                    () -> FirstPersonModelCore.instance.getConfig().sneakXOffset,
                    i -> FirstPersonModelCore.instance.getConfig().sneakXOffset = i));
            options.add(getIntOption("text.firstperson.option.firstperson.sitXOffset", -40, 40,
                    () -> FirstPersonModelCore.instance.getConfig().sitXOffset,
                    i -> FirstPersonModelCore.instance.getConfig().sitXOffset = i));
            options.add(getOnOffOption("text.firstperson.option.firstperson.renderStuckFeatures",
                    () -> FirstPersonModelCore.instance.getConfig().renderStuckFeatures,
                    b -> FirstPersonModelCore.instance.getConfig().renderStuckFeatures = b));
            options.add(getEnumOption("text.firstperson.option.firstperson.vanillaHandMode", VanillaHands.class,
                    () -> FirstPersonModelCore.instance.getConfig().vanillaHandsMode,
                    b -> FirstPersonModelCore.instance.getConfig().vanillaHandsMode = b));
            options.add(getOnOffOption("text.firstperson.option.firstperson.dynamicMode",
                    () -> FirstPersonModelCore.instance.getConfig().dynamicMode,
                    b -> FirstPersonModelCore.instance.getConfig().dynamicMode = b));
            options.add(getOnOffOption("text.firstperson.option.firstperson.vanillaHandsSkipSwimming",
                    () -> FirstPersonModelCore.instance.getConfig().vanillaHandsSkipSwimming,
                    b -> FirstPersonModelCore.instance.getConfig().vanillaHandsSkipSwimming = b));

            var optionList = createOptionList(options);
            optionList.setGap(-1);
            optionList.setSize(14 * 20, 9 * 20);

            wTabPanel.add(optionList, b -> b.title(ComponentProvider.translatable("text.firstperson.tab.settings"))
                    .icon(new ItemIcon(Items.COMPARATOR)));

            WGridPanel itemTab = createItemTab(s -> FirstPersonModelCore.instance.getConfig().autoVanillaHands.contains(getStringItem(s)), (b, s) -> {
                if (b) {
                    FirstPersonModelCore.instance.getConfig().autoToggleModItems.add(getStringItem(s));
                } else {
                    FirstPersonModelCore.instance.getConfig().autoToggleModItems.remove(getStringItem(s));
                }
                FirstPersonModelCore.instance.getLogicHandler().reloadAutoVanillaHandsSettings();
                save();
            });
            wTabPanel.add(itemTab, b -> b.title(ComponentProvider.translatable("text.firstperson.tab.autovanillahands"))
                    .icon(new ItemIcon(Items.FILLED_MAP)));

            WGridPanel disableTab = createItemTab(s -> FirstPersonModelCore.instance.getConfig().autoToggleModItems
                    .contains(getStringItem(s)), (b, s) ->{
                if (b) {
                    FirstPersonModelCore.instance.getConfig().autoToggleModItems.add(getStringItem(s));
                } else {
                    FirstPersonModelCore.instance.getConfig().autoToggleModItems.remove(getStringItem(s));
                }
                FirstPersonModelCore.instance.getLogicHandler().reloadAutoVanillaHandsSettings();
                save();
            });
            wTabPanel.add(disableTab, b -> b.title(ComponentProvider.translatable("text.firstperson.tab.disableitems"))
                    .icon(new ItemIcon(Items.BARRIER)));

            // Layers
            PlayerRendererAccess access = null;
            //? if >= 1.21.6 {

            access = (PlayerRendererAccess) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(new AvatarRenderState());
            //? } else {
            /*
            if (Minecraft.getInstance().player != null) {
                access = (PlayerRendererAccess) Minecraft.getInstance().getEntityRenderDispatcher()
                        .getRenderer(Minecraft.getInstance().player);
            }
            *///? }
            if (access != null) {
                WListPanel<RenderLayer, WToggleButton> layerList = new WListPanel<RenderLayer, WToggleButton>(
                        access.getRenderLayers(), () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                            l.setLabel(ComponentProvider.literal(s.getClass().getSimpleName()));
                            l.setToolip(ComponentProvider.literal(s.getClass().getName()));
                            l.setToggle(!FirstPersonModelCore.instance.getConfig().hiddenLayers
                                    .contains(s.getClass().getName()));
                            l.setOnToggle(b -> {
                                if (b) {
                                    FirstPersonModelCore.instance.getConfig().hiddenLayers
                                            .remove(s.getClass().getName());
                                } else {
                                    FirstPersonModelCore.instance.getConfig().hiddenLayers.add(s.getClass().getName());
                                }
                                save();
                                FirstPersonModelCore.instance.updatePlayerLayers();
                            });
                        });
                layerList.setGap(-1);
                layerList.setInsets(new Insets(2, 4));
                WGridPanel layerTab = new WGridPanel(20);
                layerTab.add(layerList, 0, 0, 17, 7);
                WTextField searchLayerField = new WTextField();
                searchLayerField.setChangedListener(s -> {
                    layerList.setFilter(e -> e.getClass().getName().toLowerCase().contains(s.toLowerCase()));
                    layerList.layout();
                });
                layerTab.add(searchLayerField, 0, 7, 17, 1);
                wTabPanel.add(layerTab, b -> b.title(ComponentProvider.translatable("text.firstperson.tab.layers"))
                        .icon(new ItemIcon(Items.NETHERITE_HELMET)));
            }

            wTabPanel.layout();
            root.add(wTabPanel, 0, 2);

            WButton doneButton = new WButton(CommonComponents.GUI_DONE);
            doneButton.setOnClick(() -> {
                save();
                Minecraft.getInstance().setScreen(previous);
            });
            root.add(doneButton, 0, 27, 6, 2);

            WButton resetButton = new WButton(ComponentProvider.translatable("controls.reset"));
            resetButton.setOnClick(() -> {
                reset();
                root.layout();
            });
            root.add(resetButton, 37, 27, 6, 2);

            root.setBackgroundPainter(BackgroundPainter.VANILLA);

            root.validate(this);
            root.setHost(this);
        }

        private static @NotNull String getStringItem(Entry<ResourceKey<Item>, Item> a) {
            return a.getKey()/*? >= 1.21.11 {*/.identifier() /*?} else {*//* .location() *//*?}*/.toString();
        }


        @Override
        public void reset() {
            FirstPersonModelCore.instance.resetSettings();
            FirstPersonModelCore.instance.writeSettings();
        }

        @Override
        public void save() {
            FirstPersonModelCore.instance.writeSettings();
        }

    }

    //    
    //        return new CustomConfigScreen(parent, "text.firstperson.title") {
    //
    //            @Override
    //            public void initialize() {
    //                FirstPersonModelCore fpm = FirstPersonModelCore.instance;
    //                getOptions().addBig(getOnOffOption("text.firstperson.option.firstperson.enabledByDefault",
    //                        () -> fpm.getConfig().enabledByDefault, b -> fpm.getConfig().enabledByDefault = b));
    //
    //                List<Object> options = new ArrayList<>();
    //                options.add(getIntOption("text.firstperson.option.firstperson.xOffset", -40, 40,
    //                        () -> fpm.getConfig().xOffset, i -> fpm.getConfig().xOffset = i));
    //                options.add(getIntOption("text.firstperson.option.firstperson.sneakXOffset", -40, 40,
    //                        () -> fpm.getConfig().sneakXOffset, i -> fpm.getConfig().sneakXOffset = i));
    //                options.add(getIntOption("text.firstperson.option.firstperson.sitXOffset", -40, 40,
    //                        () -> fpm.getConfig().sitXOffset, i -> fpm.getConfig().sitXOffset = i));
    //                options.add(getOnOffOption("text.firstperson.option.firstperson.renderStuckFeatures",
    //                        () -> fpm.getConfig().renderStuckFeatures, b -> fpm.getConfig().renderStuckFeatures = b));
    //                options.add(getEnumOption("text.firstperson.option.firstperson.vanillaHandMode", VanillaHands.class,
    //                        () -> fpm.getConfig().vanillaHandsMode, b -> fpm.getConfig().vanillaHandsMode = b));
    //                options.add(getOnOffOption("text.firstperson.option.firstperson.dynamicMode",
    //                        () -> fpm.getConfig().dynamicMode, b -> fpm.getConfig().dynamicMode = b));
    //                options.add(getOnOffOption("text.firstperson.option.firstperson.vanillaHandsSkipSwimming",
    //                        () -> fpm.getConfig().vanillaHandsSkipSwimming,
    //                        b -> fpm.getConfig().vanillaHandsSkipSwimming = b));
    //
    //                //#if MC >= 11900
    //                getOptions().addSmall(options.toArray(new OptionInstance[0]));
    //                //#else
    //                //$$getOptions().addSmall(options.toArray(new Option[0]));
    //                //#endif
    //
    //            }
    //
    //            @Override
    //            public void save() {
    //                FirstPersonModelCore.instance.writeSettings();
    //            }
    //
    //            @Override
    //            public void reset() {
    //                FirstPersonModelCore.instance.resetSettings();
    //                FirstPersonModelCore.instance.writeSettings();
    //            }
    //
    //        };
    //
    //    }

}
