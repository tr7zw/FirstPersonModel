package dev.tr7zw.firstperson.forge.config;

import java.util.function.Consumer;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.config.FirstPersonConfig;
import de.tr7zw.firstperson.config.SharedConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class ForgeConfigBuilder extends SharedConfigBuilder{

	public Screen createConfigScreen(Minecraft mc, Screen parent) {
				FirstPersonConfig config = FirstPersonModelCore.config;
		ConfigBuilder builder = ConfigBuilder.create()
		        .setParentScreen(parent)
		        .setTitle(new TranslationTextComponent("text.firstperson.title"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		
		ConfigCategory firstperson = builder.getOrCreateCategory(new TranslationTextComponent("category.firstperson.firstperson"));
		setupFirstPersonConfig(entryBuilder, firstperson, config);
		
		ConfigCategory paperdoll = builder.getOrCreateCategory(new TranslationTextComponent("category.firstperson.paperdoll"));
		setupPaperDollConfig(entryBuilder, paperdoll, config);
		
		ConfigCategory cosmetics = builder.getOrCreateCategory(new TranslationTextComponent("category.firstperson.cosmetics"));
		setupCosmeticConfig(entryBuilder, cosmetics, config);
		
		ConfigCategory skinlayer = builder.getOrCreateCategory(new TranslationTextComponent("category.firstperson.skinlayer"));
		setupSkinLayerConfig(entryBuilder, skinlayer, config);
		
		builder.setSavingRunnable(() -> {
			onSave(config);
		});
		builder.setTransparentBackground(true);
		return builder.build();
	}

	@Override
    public <T extends Enum<?>> Object createEnumSetting(Object entryBuilder, String id, Class<T> type, T value, T def, Consumer<T> save) {
    	return ((ConfigEntryBuilder)entryBuilder).startEnumSelector(new TranslationTextComponent("text.firstperson.option." + id), type, value)
		        .setDefaultValue(def)
		        .setTooltip(new TranslationTextComponent("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .setEnumNameProvider((en) -> (new TranslationTextComponent("text.firstperson.option." + id + "." + en.name())))
		        .build();
    }
    
	@Override
    public Object createBooleanSetting(Object entryBuilder, String id, Boolean value, Boolean def, Consumer<Boolean> save) {
    	return ((ConfigEntryBuilder)entryBuilder).startBooleanToggle(new TranslationTextComponent("text.firstperson.option." + id), value)
		        .setDefaultValue(def)
		        .setTooltip(new TranslationTextComponent("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
    }
    
	@Override
    public Object createIntSetting(Object entryBuilder, String id, Integer value, Integer def, Integer min, Integer max, Consumer<Integer> save) {
    	return ((ConfigEntryBuilder)entryBuilder).startIntSlider(new TranslationTextComponent("text.firstperson.option." + id), value, min, max)
		        .setDefaultValue(def)
		        .setTooltip(new TranslationTextComponent("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
    }

	@Override
	public AbstractConfigListEntry<?> getPreviewEntry() {
		return new PlayerPreviewConfigEntry();
	}

	@Override
	public void addEntry(Object category, Object entry) {
		((ConfigCategory)category).addEntry((AbstractConfigListEntry<?>) entry);
	}
	
}
