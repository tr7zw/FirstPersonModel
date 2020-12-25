package dev.tr7zw.valvet.forge.config;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.tr7zw.velvet.api.Velvet;
import dev.tr7zw.velvet.api.config.ConfigBuilder;
import dev.tr7zw.velvet.api.config.ConfigBuilder.ConfigEntryBuilder.ConfigCategory;
import dev.tr7zw.velvet.api.config.WrappedConfigEntry;
import dev.tr7zw.velvet.api.wrapper.WrappedScreen;
import dev.tr7zw.velvet.api.wrapper.WrappedText;
import me.shedaniel.clothconfig2.forge.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.forge.impl.builders.BooleanToggleBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextComponent;

public class ConfigBuilderImpl implements ConfigBuilder {

	private me.shedaniel.clothconfig2.forge.api.ConfigBuilder builder = me.shedaniel.clothconfig2.forge.api.ConfigBuilder.create();
	
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
		builder.setTitle(text.getHandler(TextComponent.class));
		return this;
	}

	@Override
	public ConfigEntryBuilder entryBuilder() {
		return new ForgeConfigEntryBuilder(builder.entryBuilder());
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
		return new ForgeConfigCategory(builder.getOrCreateCategory(id.getHandler(TextComponent.class)));
	}

	private class ForgeConfigEntryBuilder implements ConfigEntryBuilder {

		private final me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder builder;
		
		public ForgeConfigEntryBuilder(me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder builder) {
			this.builder = builder;
		}

		@Override
		public <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(WrappedText translateableText, Class<T> type, T value) {
			return new ForgeEnumSelectorBuilder<T>(builder.startEnumSelector(translateableText.getHandler(TextComponent.class), type, value));
		}
	
		private class ForgeEnumSelectorBuilder<T extends Enum<?>> implements EnumSelectorBuilder<T>{

			me.shedaniel.clothconfig2.forge.impl.builders.EnumSelectorBuilder<T> builder;
			
			public ForgeEnumSelectorBuilder(
					me.shedaniel.clothconfig2.forge.impl.builders.EnumSelectorBuilder<T> startEnumSelector) {
				this.builder = startEnumSelector;
			}

			@Override
			public EnumSelectorBuilder<T> setDefaultValue(T def) {
				builder.setDefaultValue(def);
				return (EnumSelectorBuilder<T>) this;
			}

			@Override
			public EnumSelectorBuilder<T> setTooltip(WrappedText translateableText) {
				builder.setTooltip(translateableText.getHandler(TextComponent.class));
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
				builder.setEnumNameProvider((e) -> enumNameProvider.apply((T) e).getHandler(TextComponent.class));
				return this;
			}

			@Override
			public WrappedConfigEntry build() {
				return Velvet.velvet.getWrapper().getWrappedConfigEntry(builder.build());
			}
			
		}

		@Override
		public BooleanSelectorBuilder startBooleanToggle(WrappedText translateableText, Boolean value) {
			return new ForgeBooleanSelectorBuilder(builder.startBooleanToggle(translateableText.getHandler(TextComponent.class), value));
		}
		
		private class ForgeBooleanSelectorBuilder implements BooleanSelectorBuilder{

			private BooleanToggleBuilder builder;
			
			public ForgeBooleanSelectorBuilder(BooleanToggleBuilder builder) {
				this.builder = builder;
			}

			@Override
			public BooleanSelectorBuilder setDefaultValue(Boolean def) {
				builder.setDefaultValue(def);
				return this;
			}

			@Override
			public BooleanSelectorBuilder setTooltip(WrappedText translateableText) {
				builder.setTooltip(translateableText.getHandler(TextComponent.class));
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
			return new ForgeIntSliderBuilder(builder.startIntSlider(translateableText.getHandler(TextComponent.class), value, min, max));
		}
		
		private class ForgeIntSliderBuilder implements IntSliderBuilder{

			private me.shedaniel.clothconfig2.forge.impl.builders.IntSliderBuilder builder;
			
			public ForgeIntSliderBuilder(me.shedaniel.clothconfig2.forge.impl.builders.IntSliderBuilder builder) {
				this.builder = builder;
			}

			@Override
			public IntSliderBuilder setDefaultValue(Integer def) {
				builder.setDefaultValue(def);
				return this;
			}

			@Override
			public IntSliderBuilder setTooltip(WrappedText translateableText) {
				builder.setTooltip(translateableText.getHandler(TextComponent.class));
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
	
	private class ForgeConfigCategory implements ConfigCategory{

		private me.shedaniel.clothconfig2.forge.api.ConfigCategory category;
		
		public ForgeConfigCategory(me.shedaniel.clothconfig2.forge.api.ConfigCategory category) {
			this.category = category;
		}

		@Override
		public void addEntry(WrappedConfigEntry entry) {
			category.addEntry(entry.getHandler(AbstractConfigListEntry.class));
		}
		
	}


}
