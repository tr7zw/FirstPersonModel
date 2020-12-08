package de.tr7zw.firstperson;

import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.screen.PlayerPreviewConfigEntry;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class FirstPersonModMenu implements ModMenuApi{

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return screen -> AutoConfig.getConfigScreen(FirstPersonConfig.class, screen).get();
    	return parent -> {
    		ConfigBuilder builder = ConfigBuilder.create()
    		        .setParentScreen(parent)
    		        .setTitle(new TranslatableText("text.firstperson.title"));
    		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
    		
    		ConfigCategory paperdoll = builder.getOrCreateCategory(new TranslatableText("category.firstperson.paperdoll"));
    		paperdoll.addEntry(entryBuilder.startStrField(new TranslatableText("key"), "test")
    		        .setDefaultValue("This is the default value") // Recommended: Used when user click "Reset"
    		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
    		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
    		        .build());
    		
    		ConfigCategory cosmetics = builder.getOrCreateCategory(new TranslatableText("category.firstperson.cosmetics"));
    		cosmetics.addEntry(entryBuilder.startStrField(new TranslatableText("key"), "test")
    		        .setDefaultValue("This is the default value") // Recommended: Used when user click "Reset"
    		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
    		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
    		        .build());
    		cosmetics.addEntry(new PlayerPreviewConfigEntry());
    		cosmetics.addEntry(entryBuilder.startStrField(new TranslatableText("key"), "test")
    		        .setDefaultValue("This is the default value") // Recommended: Used when user click "Reset"
    		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
    		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
    		        .build());
    		cosmetics.addEntry(entryBuilder.startEnumSelector(new TranslatableText("key"), Hat.class, Hat.VANILLA)
    		        .setDefaultValue(Hat.VANILLA) // Recommended: Used when user click "Reset"
    		        .setTooltip(new TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
    		        .setSaveConsumer(newValue -> System.out.println(newValue)) // Recommended: Called when user save the config
    		        .setEnumNameProvider((en) -> new LiteralText(en.name().toLowerCase()))
    		        .build());
    		
    		builder.setSavingRunnable(() -> {
    			// on save
    		});
    		return builder.build();
    	};
    }
}
  