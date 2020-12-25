package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.forge.config.ConfigBuilder;
import dev.tr7zw.velvet.api.Velvet;
import me.shedaniel.clothconfig2.forge.gui.ClothConfigScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

//GameMenuScreenMixin
@Mixin(IngameMenuScreen.class)
public abstract class IngameMenuScreenMixin extends Screen {

	protected IngameMenuScreenMixin(ITextComponent titleIn) {
		super(titleIn);
	}

	@Inject(method = "addButtons", at = @At("RETURN"))
	private void addButtons(CallbackInfo info) {
		this.addButton(new Button(this.width - 100, 24, 98, 20,
				new TranslationTextComponent("category.firstperson.cosmetics"), (buttonWidgetx) -> {
					ClothConfigScreen screen = (ClothConfigScreen) new ConfigBuilder()
							.createConfigScreen(Velvet.velvet.getWrapper().wrapScreen(this)).getHandler(Screen.class);
					screen.selectedCategoryIndex = 2;
					this.minecraft.displayGuiScreen((Screen) screen);
				}));
	}

}
