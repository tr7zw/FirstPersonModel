package dev.tr7zw.firstperson.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.PlayerRendererAccess;
import dev.tr7zw.firstperson.versionless.config.VanillaHands;
import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.transition.mc.ItemUtil;
import dev.tr7zw.trender.gui.client.AbstractConfigScreen;
import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WListPanel;
import dev.tr7zw.trender.gui.widget.WTabPanel;
import dev.tr7zw.trender.gui.widget.WTextField;
import dev.tr7zw.trender.gui.widget.WToggleButton;
import dev.tr7zw.trender.gui.widget.data.Insets;
import dev.tr7zw.trender.gui.widget.icon.ItemIcon;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
//#if MC >= 12106
//import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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

            List<Entry<ResourceKey<Item>, Item>> items = new ArrayList<>(ItemUtil.getItems());
            items.sort((a, b) -> a.getKey().location().toString().compareTo(b.getKey().location().toString()));

            WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton> itemList = new WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton>(
                    items, () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                        l.setLabel(s.getValue().getName(s.getValue().getDefaultInstance()));
                        l.setToolip(ComponentProvider.literal(s.getKey().location().toString()));
                        l.setIcon(new ItemIcon(s.getValue()));
                        l.setToggle(FirstPersonModelCore.instance.getConfig().autoVanillaHands
                                .contains(s.getKey().location().toString()));
                        l.setOnToggle(b -> {
                            if (b) {
                                FirstPersonModelCore.instance.getConfig().autoVanillaHands
                                        .add(s.getKey().location().toString());
                            } else {
                                FirstPersonModelCore.instance.getConfig().autoVanillaHands
                                        .remove(s.getKey().location().toString());
                            }
                            FirstPersonModelCore.instance.getLogicHandler().reloadAutoVanillaHandsSettings();
                            save();
                        });
                    });
            itemList.setGap(-1);
            itemList.setInsets(new Insets(2, 4));
            WGridPanel itemTab = new WGridPanel(20);
            itemTab.add(itemList, 0, 0, 17, 7);
            WTextField searchField = new WTextField();
            searchField.setChangedListener(s -> {
                itemList.setFilter(e -> e.getKey().location().toString().toLowerCase().contains(s.toLowerCase()));
                itemList.layout();
            });
            itemTab.add(searchField, 0, 7, 17, 1);
            wTabPanel.add(itemTab, b -> b.title(ComponentProvider.translatable("text.firstperson.tab.autovanillahands"))
                    .icon(new ItemIcon(Items.FILLED_MAP)));

            WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton> disableList = new WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton>(
                    items, () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                        l.setLabel(s.getValue().getName(s.getValue().getDefaultInstance()));
                        l.setToolip(ComponentProvider.literal(s.getKey().location().toString()));
                        l.setIcon(new ItemIcon(s.getValue()));
                        l.setToggle(FirstPersonModelCore.instance.getConfig().autoToggleModItems
                                .contains(s.getKey().location().toString()));
                        l.setOnToggle(b -> {
                            if (b) {
                                FirstPersonModelCore.instance.getConfig().autoToggleModItems
                                        .add(s.getKey().location().toString());
                            } else {
                                FirstPersonModelCore.instance.getConfig().autoToggleModItems
                                        .remove(s.getKey().location().toString());
                            }
                            FirstPersonModelCore.instance.getLogicHandler().reloadAutoVanillaHandsSettings();
                            save();
                        });
                    });
            disableList.setGap(-1);
            disableList.setInsets(new Insets(2, 4));
            WGridPanel disableTab = new WGridPanel(20);
            disableTab.add(disableList, 0, 0, 17, 7);
            WTextField searchDisableField = new WTextField();
            searchDisableField.setChangedListener(s -> {
                disableList.setFilter(e -> e.getKey().location().toString().toLowerCase().contains(s.toLowerCase()));
                disableList.layout();
            });
            disableTab.add(searchDisableField, 0, 7, 17, 1);
            wTabPanel.add(disableTab, b -> b.title(ComponentProvider.translatable("text.firstperson.tab.disableitems"))
                    .icon(new ItemIcon(Items.BARRIER)));

            // Layers
            PlayerRendererAccess access = null;
            //#if MC >= 12106
            access = (PlayerRendererAccess) Minecraft.getInstance().getEntityRenderDispatcher()
                    .getRenderer(new AvatarRenderState());
            //#else
            //$$if (Minecraft.getInstance().player != null) {
            //$$    access = (PlayerRendererAccess) Minecraft.getInstance().getEntityRenderDispatcher()
            //$$            .getRenderer(Minecraft.getInstance().player);
            //$$}
            //#endif
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
