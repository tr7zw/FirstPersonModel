package de.tr7zw.firstperson.config;

import java.util.function.Consumer;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.screen.PlayerPreviewConfigEntry;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class FirstPersonModMenu implements ModMenuApi{

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
    		setupCosmeticConfig(entryBuilder, cosmetics);
    		
    		builder.setSavingRunnable(() -> {
    			// on save
    		});
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
    
    private void setupCosmeticConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category) {
    	category.addEntry(entryBuilder.startStrField(new TranslatableText("key"), "test")
		        .setDefaultValue("This is the default value") // Recommended: Used when user click "Reset"
		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
		        .build());
    	category.addEntry(new PlayerPreviewConfigEntry());
    	category.addEntry(entryBuilder.startStrField(new TranslatableText("key"), "test")
		        .setDefaultValue("This is the default value") // Recommended: Used when user click "Reset"
		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
		        .build());
    	category.addEntry(entryBuilder.startEnumSelector(new TranslatableText("key"), Hat.class, Hat.VANILLA)
		        .setDefaultValue(Hat.VANILLA) // Recommended: Used when user click "Reset"
		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
		        .setEnumNameProvider((en) -> new LiteralText(en.name().toLowerCase()))
		        .build());
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
  