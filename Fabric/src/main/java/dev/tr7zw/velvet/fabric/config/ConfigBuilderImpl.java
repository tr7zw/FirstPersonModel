package dev.tr7zw.velvet.fabric.config;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.tr7zw.velvet.api.Velvet;
import dev.tr7zw.velvet.api.config.ConfigBuilder;
import dev.tr7zw.velvet.api.config.ConfigBuilder.ConfigEntryBuilder.ConfigCategory;
import dev.tr7zw.velvet.api.config.WrappedConfigEntry;
import dev.tr7zw.velvet.api.wrapper.WrappedScreen;
import dev.tr7zw.velvet.api.wrapper.WrappedText;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigBuilderImpl implements ConfigBuilder {

	private me.shedaniel.clothconfig2.api.ConfigBuilder builder = me.shedaniel.clothconfig2.api.ConfigBuilder.create();
	
	@Override
	public WrappedScreen build() {
		return Velvet.velvet.getWrapper().wrapScreen(builder.build());
	}

	@Override
	public ConfigBuilder setParentScreen(WrappedScreen parent) {
		builder.setParentScreen(parent.getHandler(Screen.class));
		return this;
	}

	@Override
	public ConfigBuilder setTitle(WrappedText text) {
		builder.setTitle(text.getHandler(Text.class));
		return this;
	}

	@Override
	public ConfigEntryBuilder entryBuilder() {
		return new FabricConfigEntryBuilder(builder.entryBuilder());
	}

	@Override
	public ConfigBuilder setTransparentBackground(boolean transparent) {
		builder.setTransparentBackground(transparent);
		return this;
	}

	@Override
	public ConfigBuilder setSavingRunnable(Runnable runnable) {
		builder.setSavingRunnable(runnable);
		return this;
	}

	@Override
	public ConfigCategory getOrCreateCategory(WrappedText id) {
		return new FabricConfigCategory(builder.getOrCreateCategory(id.getHandler(Text.class)));
	}

	private class FabricConfigEntryBuilder implements ConfigEntryBuilder {

		private final me.shedaniel.clothconfig2.api.ConfigEntryBuilder builder;
		
		public FabricConfigEntryBuilder(me.shedaniel.clothconfig2.api.ConfigEntryBuilder builder) {
			this.builder = builder;
		}

		@Override
		public <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(WrappedText translateableText, Class<T> type, T value) {
			return new FabricEnumSelectorBuilder<T>(builder.startEnumSelector(translateableText.getHandler(Text.class), type, value));
		}
	
		private class FabricEnumSelectorBuilder<T extends Enum<?>> implements EnumSelectorBuilder<T>{

			me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder<T> builder;
			
			public FabricEnumSelectorBuilder(
					me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder<T> startEnumSelector) {
				this.builder = startEnumSelector;
			}

			@Override
			public EnumSelectorBuilder<T> setDefaultValue(T def) {
				builder.setDefaultValue(def);
				return (EnumSelectorBuilder<T>) this;
			}

			@Override
			public EnumSelectorBuilder<T> setTooltip(WrappedText translateableText) {
				builder.setTooltip(translateableText.getHandler(Text.class));
				return this;
			}

			@Override
			public EnumSelectorBuilder<T> setSaveConsumer(Consumer<T> save) {
				builder.setSaveConsumer(save);
				return this;
			}

			@SuppressWarnings("unchecked")
			@Override
			public EnumSelectorBuilder<T> setEnumNameProvider(
					Function<T, WrappedText> enumNameProvider) {
				builder.setEnumNameProvider((e) -> enumNameProvider.apply((T) e).getHandler(Text.class));
				return this;
			}

			@Override
			public WrappedConfigEntry build() {
				return Velvet.velvet.getWrapper().getWrappedConfigEntry(builder.build());
			}
			
		}

		@Override
		public BooleanSelectorBuilder startBooleanToggle(WrappedText translateableText, Boolean value) {
			return new FabricBooleanSelectorBuilder(builder.startBooleanToggle(translateableText.getHandler(Text.class), value));
		}
		
		private class FabricBooleanSelectorBuilder implements BooleanSelectorBuilder{

			private BooleanToggleBuilder builder;
			
			public FabricBooleanSelectorBuilder(BooleanToggleBuilder builder) {
				this.builder = builder;
			}

			@Override
			public BooleanSelectorBuilder setDefaultValue(Boolean def) {
				builder.setDefaultValue(def);
				return this;
			}

			@Override
			public BooleanSelectorBuilder setTooltip(WrappedText translateableText) {
				builder.setTooltip(translateableText.getHandler(Text.class));
				return this;
			}

			@Override
			public BooleanSelectorBuilder setSaveConsumer(Consumer<Boolean> save) {
				builder.setSaveConsumer(save);
				return this;
			}

			@Override
			public WrappedConfigEntry build() {
				return Velvet.velvet.getWrapper().getWrappedConfigEntry(builder.build());
			}
			
		}

		@Override
		public IntSliderBuilder startIntSlider(WrappedText translateableText, Integer value, Integer min, Integer max) {
			return new FabricIntSliderBuilder(builder.startIntSlider(translateableText.getHandler(Text.class), value, min, max));
		}
		
		private class FabricIntSliderBuilder implements IntSliderBuilder{

			private me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder builder;
			
			public FabricIntSliderBuilder(me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder builder) {
				this.builder = builder;
			}

			@Override
			public IntSliderBuilder setDefaultValue(Integer def) {
				builder.setDefaultValue(def);
				return this;
			}

			@Override
			public IntSliderBuilder setTooltip(WrappedText translateableText) {
				builder.setTooltip(translateableText.getHandler(Text.class));
				return this;
			}

			@Override
			public IntSliderBuilder setSaveConsumer(Consumer<Integer> save) {
				builder.setSaveConsumer(save);
				return this;
			}

			@Override
			public WrappedConfigEntry build() {
				return Velvet.velvet.getWrapper().getWrappedConfigEntry(builder.build());
			}
			
		}
		
	}
	
	private class FabricConfigCategory implements ConfigCategory{

		private me.shedaniel.clothconfig2.api.ConfigCategory category;
		
		public FabricConfigCategory(me.shedaniel.clothconfig2.api.ConfigCategory category) {
			this.category = category;
		}

		@Override
		public void addEntry(WrappedConfigEntry entry) {
			category.addEntry(entry.getHandler(AbstractConfigListEntry.class));
		}
		
	}


}
