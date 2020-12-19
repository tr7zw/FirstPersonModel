package de.tr7zw.firstperson.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import de.tr7zw.firstperson.features.LayerMode;

public abstract class SharedConfigBuilder {

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static Object hatSelection = null;
	public static Object headSelection = null;
	public static Object chestSelection = null;
	public static Object backSelection = null;
	public static Object bootsSelection = null;
	public static Object sizeSelection = null;

	public void onSave(FirstPersonConfig config) {
		// on save
		File settingsFile = new File("config", "firstperson.json");
		if(settingsFile.exists())settingsFile.delete();
		try {
			Files.write(settingsFile.toPath(), gson.toJson(config).getBytes(StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FirstPersonModelCore.syncManager.checkForUpdates();
		new Thread(() -> {
			try {
				Thread.sleep(1000);
				FirstPersonModelCore.instance.getWrapper().refreshPlayerSettings();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public void setupFirstPersonConfig(Object entryBuilder, Object category, FirstPersonConfig config) {
		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.enabledByDefault",
				config.firstPerson.enabledByDefault, true, n -> config.firstPerson.enabledByDefault = n));
		addEntry(category, createIntSetting(entryBuilder, "firstperson.xOffset", config.firstPerson.xOffset, 0, -40, 40,
				n -> config.firstPerson.xOffset = n));
		addEntry(category, createIntSetting(entryBuilder, "firstperson.sneakXOffset", config.firstPerson.sneakXOffset,
				0, -40, 40, n -> config.firstPerson.sneakXOffset = n));
		addEntry(category, createIntSetting(entryBuilder, "firstperson.sitXOffset", config.firstPerson.sitXOffset, 0,
				-40, 40, n -> config.firstPerson.sitXOffset = n));
		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.vanillaHands",
				config.firstPerson.vanillaHands, false, n -> config.firstPerson.vanillaHands = n));
		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.forceActive", config.firstPerson.forceActive,
				false, n -> config.firstPerson.forceActive = n));
	}

	public void setupPaperDollConfig(Object entryBuilder, Object category, FirstPersonConfig config) {
		addEntry(category, createBooleanSetting(entryBuilder, "doll.Enabled", config.paperDoll.dollEnabled, false,
				n -> config.paperDoll.dollEnabled = n));
		addEntry(category, createIntSetting(entryBuilder, "doll.XOffset", config.paperDoll.dollXOffset, 0, -40, 40,
				n -> config.paperDoll.dollXOffset = n));
		addEntry(category, createIntSetting(entryBuilder, "doll.YOffset", config.paperDoll.dollYOffset, 0, -40, 40,
				n -> config.paperDoll.dollYOffset = n));
		addEntry(category, createIntSetting(entryBuilder, "doll.Size", config.paperDoll.dollSize, 0, -40, 40,
				n -> config.paperDoll.dollSize = n));
		addEntry(category, createIntSetting(entryBuilder, "doll.LookingSides", config.paperDoll.dollLookingSides, 20,
				-80, 80, n -> config.paperDoll.dollLookingSides = n));
		addEntry(category, createIntSetting(entryBuilder, "doll.LookingUpDown", config.paperDoll.dollLookingUpDown, -20,
				-80, 80, n -> config.paperDoll.dollLookingUpDown = n));
		addEntry(category, createEnumSetting(entryBuilder, "doll.headmode", PaperDollSettings.DollHeadMode.class, config.paperDoll.dollHeadMode, PaperDollSettings.DollHeadMode.FREE,
				n -> config.paperDoll.dollHeadMode = n));
	}

	public void setupCosmeticConfig(Object entryBuilder, Object category, FirstPersonConfig config) {
		hatSelection = createEnumSetting(entryBuilder, "cosmetic.hat", Hat.class, config.cosmetic.hat, Hat.VANILLA,
				n -> config.cosmetic.hat = n);
		addEntry(category, hatSelection);
		headSelection = createEnumSetting(entryBuilder, "cosmetic.head", Head.class, config.cosmetic.head, Head.VANILLA,
				n -> config.cosmetic.head = n);
		addEntry(category, headSelection);
		chestSelection = createEnumSetting(entryBuilder, "cosmetic.chest", Chest.class, config.cosmetic.chest,
				Chest.VANILLA, n -> config.cosmetic.chest = n);
		addEntry(category, chestSelection);
		backSelection = createEnumSetting(entryBuilder, "cosmetic.back", Back.class, config.cosmetic.back, Back.VANILLA,
				n -> config.cosmetic.back = n);
		addEntry(category, backSelection);
		bootsSelection = createEnumSetting(entryBuilder, "cosmetic.boots", Boots.class, config.cosmetic.boots,
				Boots.VANILLA, n -> config.cosmetic.boots = n);
		addEntry(category, bootsSelection);
		sizeSelection = createIntSetting(entryBuilder, "cosmetic.playerSize", config.cosmetic.playerSize, 100, 70, 100,
				n -> config.cosmetic.playerSize = n);
		addEntry(category, sizeSelection);
		addEntry(category, createBooleanSetting(entryBuilder, "cosmetic.modifyCameraHeight",
				config.cosmetic.modifyCameraHeight, false, n -> config.cosmetic.modifyCameraHeight = n));
		addEntry(category, getPreviewEntry());
	}

	public void setupSkinLayerConfig(Object entryBuilder, Object category, FirstPersonConfig config) {
		addEntry(category, createEnumSetting(entryBuilder, "skinlayer.headLayerMode", LayerMode.class,
				config.skinLayer.headLayerMode, LayerMode.VANILLA2D, n -> config.skinLayer.headLayerMode = n));
		addEntry(category, createIntSetting(entryBuilder, "skinlayer.optimizedLayerDistance",
				config.skinLayer.optimizedLayerDistance, 16, 8, 32, n -> config.skinLayer.optimizedLayerDistance = n));
		addEntry(category, createEnumSetting(entryBuilder, "skinlayer.skinLayerMode", LayerMode.class,
				config.skinLayer.skinLayerMode, LayerMode.VANILLA2D, n -> config.skinLayer.skinLayerMode = n));
	}

	public abstract <T extends Enum<?>> Object createEnumSetting(Object entryBuilder, String id, Class<T> type, T value,
			T def, Consumer<T> save);

	public abstract Object createBooleanSetting(Object entryBuilder, String id, Boolean value, Boolean def,
			Consumer<Boolean> save);

	public abstract Object createIntSetting(Object entryBuilder, String id, Integer value, Integer def, Integer min,
			Integer max, Consumer<Integer> save);

	public abstract Object getPreviewEntry();

	public abstract void addEntry(Object category, Object entry);

}
