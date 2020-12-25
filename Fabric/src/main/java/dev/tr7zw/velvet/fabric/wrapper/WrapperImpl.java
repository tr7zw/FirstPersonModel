package dev.tr7zw.velvet.fabric.wrapper;

import dev.tr7zw.velvet.api.config.WrappedConfigEntry;
import dev.tr7zw.velvet.api.wrapper.WrappedScreen;
import dev.tr7zw.velvet.api.wrapper.WrappedText;
import dev.tr7zw.velvet.api.wrapper.Wrapper;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class WrapperImpl implements Wrapper{

	@Override
	public WrappedScreen wrapScreen(Object screen) {
		return new WrappedScreen() {
			
			private Screen handler = (Screen) screen;
			
			@Override
			public Screen getHandler() {
				return handler;
			}
		};
	}

	@Override
	public WrappedText wrapText(Object text) {
		return new WrappedText() {
			
			private Text handler = (Text) text;
			
			@Override
			public Object getHandler() {
				return handler;
			}
		};
	}

	@Override
	public WrappedText getTranslateableText(String text) {
		return wrapText(new TranslatableText(text));
	}

	@Override
	public WrappedConfigEntry getWrappedConfigEntry(Object entry) {
		return new WrappedConfigEntry() {
			
			private AbstractConfigEntry<?> handler = (AbstractConfigEntry<?>) entry;
			
			@Override
			public Object getHandler() {
				return handler;
			}

			@SuppressWarnings("unchecked")
			@Override
			public <T extends Enum<?>> T getEnumValue(Class<T> targetEnum) {
				return ((EnumListEntry<T>)handler).getValue();
			}

			@Override
			public int getIntValue() {
				return (int) handler.getValue();
			}
		};
	}

}
