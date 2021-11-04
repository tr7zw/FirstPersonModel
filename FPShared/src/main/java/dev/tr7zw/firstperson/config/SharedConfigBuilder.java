//package dev.tr7zw.firstperson.config;
//
//import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.util.function.Consumer;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import dev.tr7zw.firstperson.FirstPersonModelCore;
//import dev.tr7zw.firstperson.features.Back;
//import dev.tr7zw.firstperson.features.Boots;
//import dev.tr7zw.firstperson.features.Chest;
//import dev.tr7zw.firstperson.features.Hat;
//import dev.tr7zw.firstperson.features.Head;
//import dev.tr7zw.firstperson.util.PaperDollLocation;
//import dev.tr7zw.transliterationlib.api.config.ConfigBuilder;
//import dev.tr7zw.transliterationlib.api.config.ConfigBuilder.ConfigEntryBuilder;
//import dev.tr7zw.transliterationlib.api.config.ConfigBuilder.ConfigEntryBuilder.ConfigCategory;
//import dev.tr7zw.transliterationlib.api.config.WrappedConfigEntry;
//import dev.tr7zw.transliterationlib.api.wrapper.WrappedScreen;
//
//public abstract class SharedConfigBuilder {
//
//	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
//	public static WrappedConfigEntry hatSelection = null;
//	public static WrappedConfigEntry headSelection = null;
//	public static WrappedConfigEntry chestSelection = null;
//	public static WrappedConfigEntry backSelection = null;
//	public static WrappedConfigEntry bootsSelection = null;
//	public static WrappedConfigEntry sizeSelection = null;
//	public static WrappedConfigEntry backHueSelection = null;
//
//	public WrappedScreen createConfigScreen(WrappedScreen parentScreen) {
//		FirstPersonConfig config = FirstPersonModelCore.config;
//		ConfigBuilder builder = transliteration.getNewConfigBuilder();
//		builder.setParentScreen(parentScreen).setTitle(transliteration.getWrapper().getTranslateableText("text.firstperson.title"));
//		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
//		
//		ConfigCategory firstperson = builder.getOrCreateCategory(transliteration.getWrapper().getTranslateableText("category.firstperson.firstperson"));
//		setupFirstPersonConfig(entryBuilder, firstperson, config);
//		
//		ConfigCategory paperdoll = builder.getOrCreateCategory(transliteration.getWrapper().getTranslateableText("category.firstperson.paperdoll"));
//		setupPaperDollConfig(entryBuilder, paperdoll, config);
//		
//		ConfigCategory cosmetics = builder.getOrCreateCategory(transliteration.getWrapper().getTranslateableText("category.firstperson.cosmetics"));
//		setupCosmeticConfig(entryBuilder, cosmetics, config);
//		
//		builder.setSavingRunnable(() -> {
//			// on save
//			File settingsFile = new File("config", "firstperson.json");
//			if(settingsFile.exists())settingsFile.delete();
//			try {
//				Files.write(settingsFile.toPath(), gson.toJson(config).getBytes(StandardCharsets.UTF_8));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			FirstPersonModelCore.syncManager.checkForUpdates();
//			new Thread(() -> {
//				try {
//					Thread.sleep(1000);
//					FirstPersonModelCore.wrapper.refreshPlayerSettings();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}).start();
//		});
//		builder.setTransparentBackground(true);
//		
//		return builder.build();
//	}
//	
//	public void setupFirstPersonConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
//		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.enabledByDefault",
//				config.firstPerson.enabledByDefault, true, n -> config.firstPerson.enabledByDefault = n));
//		addEntry(category, createIntSetting(entryBuilder, "firstperson.xOffset", config.firstPerson.xOffset, 0, -40, 40,
//				n -> config.firstPerson.xOffset = n));
//		addEntry(category, createIntSetting(entryBuilder, "firstperson.sneakXOffset", config.firstPerson.sneakXOffset,
//				0, -40, 40, n -> config.firstPerson.sneakXOffset = n));
//		addEntry(category, createIntSetting(entryBuilder, "firstperson.sitXOffset", config.firstPerson.sitXOffset, 0,
//				-40, 40, n -> config.firstPerson.sitXOffset = n));
//		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.vanillaHands",
//				config.firstPerson.vanillaHands, false, n -> config.firstPerson.vanillaHands = n));
//		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.doubleHands",
//				config.firstPerson.doubleHands, false, n -> config.firstPerson.doubleHands = n));
//		addEntry(category, createBooleanSetting(entryBuilder, "firstperson.forceActive", config.firstPerson.forceActive,
//				false, n -> config.firstPerson.forceActive = n));
//	}
//
//	public void setupPaperDollConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
//		addEntry(category, createBooleanSetting(entryBuilder, "doll.Enabled", config.paperDoll.dollEnabled, false,
//				n -> config.paperDoll.dollEnabled = n));
//		addEntry(category, createEnumSetting(entryBuilder, "doll.location", PaperDollLocation.class, config.paperDoll.location, PaperDollLocation.TOP_LEFT,
//				n -> config.paperDoll.location = n));
//		addEntry(category, createEnumSetting(entryBuilder, "doll.headmode", PaperDollSettings.DollHeadMode.class, config.paperDoll.dollHeadMode, PaperDollSettings.DollHeadMode.FREE,
//				n -> config.paperDoll.dollHeadMode = n));
//		addEntry(category, createIntSetting(entryBuilder, "doll.XOffset", config.paperDoll.dollXOffset, 0, -40, 40,
//				n -> config.paperDoll.dollXOffset = n));
//		addEntry(category, createIntSetting(entryBuilder, "doll.YOffset", config.paperDoll.dollYOffset, 0, -40, 40,
//				n -> config.paperDoll.dollYOffset = n));
//		addEntry(category, createIntSetting(entryBuilder, "doll.Size", config.paperDoll.dollSize, 0, -40, 40,
//				n -> config.paperDoll.dollSize = n));
//		addEntry(category, createIntSetting(entryBuilder, "doll.LookingSides", config.paperDoll.dollLookingSides, 20,
//				-80, 80, n -> config.paperDoll.dollLookingSides = n));
//		addEntry(category, createIntSetting(entryBuilder, "doll.LookingUpDown", config.paperDoll.dollLookingUpDown, -20,
//				-80, 80, n -> config.paperDoll.dollLookingUpDown = n));
//	}
//
//	public void setupCosmeticConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, FirstPersonConfig config) {
//		hatSelection = createEnumSetting(entryBuilder, "cosmetic.hat", Hat.class, config.cosmetic.hat, Hat.VANILLA,
//				n -> config.cosmetic.hat = n);
//		addEntry(category, hatSelection);
//		headSelection = createEnumSetting(entryBuilder, "cosmetic.head", Head.class, config.cosmetic.head, Head.VANILLA,
//				n -> config.cosmetic.head = n);
//		addEntry(category, headSelection);
//		chestSelection = createEnumSetting(entryBuilder, "cosmetic.chest", Chest.class, config.cosmetic.chest,
//				Chest.VANILLA, n -> config.cosmetic.chest = n);
//		addEntry(category, chestSelection);
//		backSelection = createEnumSetting(entryBuilder, "cosmetic.back", Back.class, config.cosmetic.back, Back.VANILLA,
//				n -> config.cosmetic.back = n);
//		addEntry(category, backSelection);
//		backHueSelection = createIntSetting(entryBuilder, "cosmetic.backHue", config.cosmetic.backHue, 0,
//				0, 360, n ->  config.cosmetic.backHue = n);
//		addEntry(category, backHueSelection);
//		bootsSelection = createEnumSetting(entryBuilder, "cosmetic.boots", Boots.class, config.cosmetic.boots,
//				Boots.VANILLA, n -> config.cosmetic.boots = n);
//		addEntry(category, bootsSelection);
//		sizeSelection = createIntSetting(entryBuilder, "cosmetic.playerSize", config.cosmetic.playerSize, 100, 70, 100,
//				n -> config.cosmetic.playerSize = n);
//		addEntry(category, sizeSelection);
//		addEntry(category, createBooleanSetting(entryBuilder, "cosmetic.modifyCameraHeight",
//				config.cosmetic.modifyCameraHeight, false, n -> config.cosmetic.modifyCameraHeight = n));
//		addEntry(category, getPreviewEntry());
//	}
//
//	public <T extends Enum<?>> WrappedConfigEntry createEnumSetting(ConfigEntryBuilder entryBuilder, String id, Class<T> type, T value,
//			T def, Consumer<T> save) {
//    	return entryBuilder.startEnumSelector(transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id), type, value)
//		        .setDefaultValue(def)
//		        .setTooltip(transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id + ".@Tooltip"))
//		        .setSaveConsumer(save)
//		        .setEnumNameProvider((en) -> (transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id + "." + en.name())))
//		        .build();
//    }
//
//	public WrappedConfigEntry createBooleanSetting(ConfigEntryBuilder entryBuilder, String id, Boolean value, Boolean def,
//			Consumer<Boolean> save) {
//    	return entryBuilder.startBooleanToggle(transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id), value)
//		        .setDefaultValue(def)
//		        .setTooltip(transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id + ".@Tooltip"))
//		        .setSaveConsumer(save)
//		        .build();
//	}
//
//	public WrappedConfigEntry createIntSetting(ConfigEntryBuilder entryBuilder, String id, Integer value, Integer def, Integer min,
//			Integer max, Consumer<Integer> save) {
//    	return entryBuilder.startIntSlider(transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id), value, min, max)
//		        .setDefaultValue(def)
//		        .setTooltip(transliteration.getWrapper().getTranslateableText("text.firstperson.option." + id + ".@Tooltip"))
//		        .setSaveConsumer(save)
//		        .build();
//	}
//
//	public abstract WrappedConfigEntry getPreviewEntry();
//
//	public void addEntry(ConfigCategory category, WrappedConfigEntry entry) {
//		category.addEntry(entry);
//	}
//
//}
