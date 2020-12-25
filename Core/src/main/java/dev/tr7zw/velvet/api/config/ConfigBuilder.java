package dev.tr7zw.velvet.api.config;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.tr7zw.velvet.api.config.ConfigBuilder.ConfigEntryBuilder.ConfigCategory;
import dev.tr7zw.velvet.api.wrapper.WrappedScreen;
import dev.tr7zw.velvet.api.wrapper.WrappedText;

/**
 * A Mod loader independed Wrapper for ClothConfig to make it
 * 
 * @author tr7zw
 *
 */
public interface ConfigBuilder {


	/**
	 * Returns the NMS Screen Object
	 * 
	 * @return
	 */
	public abstract WrappedScreen build();

	public ConfigBuilder setParentScreen(WrappedScreen parent);

	public ConfigBuilder setTitle(WrappedText text);

	public abstract ConfigEntryBuilder entryBuilder();

	public ConfigBuilder setTransparentBackground(boolean transparent);

	public ConfigBuilder setSavingRunnable(Runnable runnable);
	
	public ConfigCategory getOrCreateCategory(WrappedText id);

	public interface ConfigEntryBuilder {

		public interface ConfigCategory {

			void addEntry(WrappedConfigEntry entry);

		}

		public <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(WrappedText translateableText, Class<T> type, T value);
		
		public interface EnumSelectorBuilder<T>{

			public EnumSelectorBuilder<T> setDefaultValue(T def);

			public EnumSelectorBuilder<T> setTooltip(WrappedText translateableText);

			public EnumSelectorBuilder<T> setSaveConsumer(Consumer<T> save);

			public EnumSelectorBuilder<T> setEnumNameProvider(Function<T, WrappedText> enumNameProvider);

			public WrappedConfigEntry build();
			
		}

		public BooleanSelectorBuilder startBooleanToggle(WrappedText translateableText, Boolean value);
		
		public interface BooleanSelectorBuilder{

			public BooleanSelectorBuilder setDefaultValue(Boolean def);

			public BooleanSelectorBuilder setTooltip(WrappedText translateableText);

			public BooleanSelectorBuilder setSaveConsumer(Consumer<Boolean> save);

			public WrappedConfigEntry build();
			
		}

		public IntSliderBuilder startIntSlider(WrappedText translateableText, Integer value, Integer min,
				Integer max);
		
		public interface IntSliderBuilder{

			IntSliderBuilder setDefaultValue(Integer def);

			IntSliderBuilder setTooltip(WrappedText translateableText);

			IntSliderBuilder setSaveConsumer(Consumer<Integer> save);

			WrappedConfigEntry build();
			
		}
	}

}
