package de.tr7zw.firstperson.config;

import java.util.function.Consumer;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import de.tr7zw.firstperson.features.LayerMode;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

public class FirstPersonModMenu implements ModMenuApi{

	public static EnumListEntry<Hat> hatSelection = null;
	public static EnumListEntry<Head> headSelection = null;
	public static EnumListEntry<Chest> chestSelection = null;
	public static EnumListEntry<Back> backSelection = null;
	public static EnumListEntry<Boots> bootsSelection = null;
	public static IntegerSliderEntry sizeSelection = null;
	
    @SuppressWarnings("resource")
	@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return screen -> AutoConfig.getConfigScreen(FirstPersonConfig.class, screen).get();
    	return parent -> {
    		FirstPersonConfig config = FirstPersonModelMod.config;
    		ConfigBuilder builder = ConfigBuilder.create()
    		        .setParentScreen(parent)
    		        .setTitle(new TranslatableText("text.firstperson.title"));
    		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
    		
    		ConfigCategory firstperson = builder.getOrCreateCategory(new TranslatableText("category.firstperson.firstperson"));
    		setupFirstPersonConfig(entryBuilder, firstperson, config);
    		
    		ConfigCategory paperdoll = builder.getOrCreateCategory(new TranslatableText("category.firstperson.paperdoll"));
    		setupPaperDollConfig(entryBuilder, paperdoll, config);
    		
    		ConfigCategory cosmetics = builder.getOrCreateCategory(new TranslatableText("category.firstperson.cosmetics"));
    		setupCosmeticConfig(entryBuilder, cosmetics, config);
    		
    		ConfigCategory skinlayer = builder.getOrCreateCategory(new TranslatableText("category.firstperson.skinlayer"));
    		setupSkinLayerConfig(entryBuilder, skinlayer, config);
    		
    		builder.setSavingRunnable(() -> {
    			// on save
    			
    			FirstPersonModelMod.syncManager.checkForUpdates();
    			new Thread(() -> {
    				try {
						Thread.sleep(1000);
						MinecraftClient.getInstance().options.onPlayerModelPartChange();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			}).start();
    		});
    		builder.setTransparentBackground(true);
    		return builder.build();
    	};
    }
 
    private void setupFirstPersonConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
    	category.addEntry(createBooleanSetting(entryBuilder, "firstperson.enabledByDefault", config.firstPerson.enabledByDefault, true, n -> config.firstPerson.enabledByDefault = n));
    	category.addEntry(createIntSetting(entryBuilder, "firstperson.xOffset", config.firstPerson.xOffset, 0, -40, 40,  n -> config.firstPerson.xOffset = n));
    	category.addEntry(createIntSetting(entryBuilder, "firstperson.sneakXOffset", config.firstPerson.sneakXOffset, 0, -40, 40,  n -> config.firstPerson.sneakXOffset = n));
    	category.addEntry(createIntSetting(entryBuilder, "firstperson.sitXOffset", config.firstPerson.sitXOffset, 0, -40, 40,  n -> config.firstPerson.sitXOffset = n));
    	category.addEntry(createBooleanSetting(entryBuilder, "firstperson.vanillaHands", config.firstPerson.vanillaHands, false, n -> config.firstPerson.vanillaHands = n));
    	category.addEntry(createBooleanSetting(entryBuilder, "firstperson.forceActive", config.firstPerson.forceActive, false, n -> config.firstPerson.forceActive = n));
    }
    
    private void setupPaperDollConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
    	category.addEntry(createBooleanSetting(entryBuilder, "doll.Enabled", config.paperDoll.dollEnabled, false, n -> config.paperDoll.dollEnabled = n));
    	category.addEntry(createIntSetting(entryBuilder, "doll.XOffset", config.paperDoll.dollXOffset, 0, -40, 40,  n -> config.paperDoll.dollXOffset = n));
    	category.addEntry(createIntSetting(entryBuilder, "doll.YOffset", config.paperDoll.dollYOffset, 0, -40, 40,  n -> config.paperDoll.dollYOffset = n));
    	category.addEntry(createIntSetting(entryBuilder, "doll.Size", config.paperDoll.dollSize, 0, -40, 40,  n -> config.paperDoll.dollSize = n));
    	category.addEntry(createIntSetting(entryBuilder, "doll.LookingSides", config.paperDoll.dollLookingSides, 20, -80, 80,  n -> config.paperDoll.dollLookingSides = n));
    	category.addEntry(createIntSetting(entryBuilder, "doll.LookingUpDown", config.paperDoll.dollLookingUpDown, -20, -80, 80,  n -> config.paperDoll.dollLookingUpDown = n));
    	category.addEntry(createBooleanSetting(entryBuilder, "doll.LockedHead", config.paperDoll.dollLockedHead, false, n -> config.paperDoll.dollLockedHead = n));
    }
    
    private void setupCosmeticConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
    	hatSelection = createEnumSetting(entryBuilder, "cosmetic.hat", Hat.class, config.cosmetic.hat, Hat.VANILLA, n -> config.cosmetic.hat = n);
    	category.addEntry(hatSelection);
    	headSelection = createEnumSetting(entryBuilder, "cosmetic.head", Head.class, config.cosmetic.head, Head.VANILLA, n -> config.cosmetic.head = n);
    	category.addEntry(headSelection);
    	chestSelection = createEnumSetting(entryBuilder, "cosmetic.chest", Chest.class, config.cosmetic.chest, Chest.VANILLA, n -> config.cosmetic.chest = n);
    	category.addEntry(chestSelection);
    	backSelection = createEnumSetting(entryBuilder, "cosmetic.back", Back.class, config.cosmetic.back, Back.VANILLA, n -> config.cosmetic.back = n);
    	category.addEntry(backSelection);
    	bootsSelection = createEnumSetting(entryBuilder, "cosmetic.boots", Boots.class, config.cosmetic.boots, Boots.VANILLA, n -> config.cosmetic.boots = n);
    	category.addEntry(bootsSelection);
    	sizeSelection = createIntSetting(entryBuilder, "cosmetic.playerSize", config.cosmetic.playerSize, 100, 70, 100,  n -> config.cosmetic.playerSize = n);
    	category.addEntry(sizeSelection);
    	category.addEntry(createBooleanSetting(entryBuilder, "cosmetic.modifyCameraHeight", config.cosmetic.modifyCameraHeight, false, n -> config.cosmetic.modifyCameraHeight = n));
    	category.addEntry(new PlayerPreviewConfigEntry());
    }
    
    private void setupSkinLayerConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
    	category.addEntry(createEnumSetting(entryBuilder, "skinlayer.headLayerMode", LayerMode.class, config.skinLayer.headLayerMode, LayerMode.VANILLA2D, n -> config.skinLayer.headLayerMode = n));
    	category.addEntry(createIntSetting(entryBuilder, "skinlayer.optimizedLayerDistance", config.skinLayer.optimizedLayerDistance, 16, 8, 32,  n -> config.skinLayer.optimizedLayerDistance = n));
    	category.addEntry(createEnumSetting(entryBuilder, "skinlayer.skinLayerMode", LayerMode.class, config.skinLayer.skinLayerMode, LayerMode.VANILLA2D, n -> config.skinLayer.skinLayerMode = n));
    }
    
    private <T extends Enum<?>> EnumListEntry<T> createEnumSetting(ConfigEntryBuilder entryBuilder, String id, Class<T> type, T value, T def, Consumer<T> save) {
    	return entryBuilder.startEnumSelector(new TranslatableText("text.firstperson.option." + id), type, value)
		        .setDefaultValue(def)
		        .setTooltip(new TranslatableText("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .setEnumNameProvider((en) -> (new TranslatableText("text.firstperson.option." + id + "." + en.name())))
		        .build();
    }
    
    private BooleanListEntry createBooleanSetting(ConfigEntryBuilder entryBuilder, String id, Boolean value, Boolean def, Consumer<Boolean> save) {
    	return entryBuilder.startBooleanToggle(new TranslatableText("text.firstperson.option." + id), value)
		        .setDefaultValue(def)
		        .setTooltip(new TranslatableText("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
    }
    
    private IntegerSliderEntry createIntSetting(ConfigEntryBuilder entryBuilder, String id, Integer value, Integer def, Integer min, Integer max, Consumer<Integer> save) {
    	return entryBuilder.startIntSlider(new TranslatableText("text.firstperson.option." + id), value, min, max)
		        .setDefaultValue(def)
		        .setTooltip(new TranslatableText("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
    }
    
}
  