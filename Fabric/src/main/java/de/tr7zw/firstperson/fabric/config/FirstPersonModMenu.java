package de.tr7zw.firstperson.fabric.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.config.FirstPersonConfig;
import de.tr7zw.firstperson.config.SharedConfigBuilder;
import de.tr7zw.firstperson.fabric.FirstPersonModelMod;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

public class FirstPersonModMenu extends SharedConfigBuilder implements ModMenuApi{


    @SuppressWarnings("resource")
	@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return screen -> AutoConfig.getConfigScreen(FirstPersonConfig.class, screen).get();
    	return parent -> {
    		FirstPersonConfig config = FirstPersonModelCore.config;
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
    			File settingsFile = new File("config", "firstperson.json");
    			if(settingsFile.exists())settingsFile.delete();
    			try {
					Files.write(settingsFile.toPath(), gson.toJson(config).getBytes(StandardCharsets.UTF_8));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
 
    public <T extends Enum<?>> Object createEnumSetting(Object entryBuilder, String id, Class<T> type, T value, T def, Consumer<T> save) {
    	return ((ConfigEntryBuilder)entryBuilder).startEnumSelector(new TranslatableText("text.firstperson.option." + id), type, value)
		        .setDefaultValue(def)
		        .setTooltip(new TranslatableText("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .setEnumNameProvider((en) -> (new TranslatableText("text.firstperson.option." + id + "." + en.name())))
		        .build();
    }
    
    public Object createBooleanSetting(Object entryBuilder, String id, Boolean value, Boolean def, Consumer<Boolean> save) {
    	return ((ConfigEntryBuilder)entryBuilder).startBooleanToggle(new TranslatableText("text.firstperson.option." + id), value)
		        .setDefaultValue(def)
		        .setTooltip(new TranslatableText("text.firstperson.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
    }
    
    public Object createIntSetting(Object entryBuilder, String id, Integer value, Integer def, Integer min, Integer max, Consumer<Integer> save) {
    	return ((ConfigEntryBuilder)entryBuilder).startIntSlider(new TranslatableText("text.firstperson.option." + id), value, min, max)
		        .setDefaultValue(def)
		        .setTooltip(new TranslatableText("text.firstperson.option." + id + ".@Tooltip"))
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
  