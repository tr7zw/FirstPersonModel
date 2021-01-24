package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.config.ConfigBuilder;
import dev.tr7zw.velvet.api.Velvet;
import me.shedaniel.clothconfig2.forge.gui.ClothConfigScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

//GameMenuScreenMixin
@Mixin(IngameMenuScreen.class)
public abstract class IngameMenuScreenMixin extends Screen {

	protected IngameMenuScreenMixin(ITextComponent titleIn) {
		super(titleIn);
	}
	
	private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("firstperson", "textures/gui/cosmetics.png");

	@Inject(method = "addButtons", at = @At("RETURN"))
	private void addButtons(CallbackInfo info) {
		if(FirstPersonModelCore.config == null || Velvet.velvet == null)return; // Forge sometimes renders the ingame menu while loading mods?!?
		if (!FirstPersonModelCore.config.firstPerson.hideCosmeticsButton) {
			this.addButton(new ImageButton(this.width / 2 - 102 - 24, this.height / 4 + 96 + -16, 20, 20, 0, 0, 20, BUTTON_TEXTURE, 32, 64,
					(buttonWidgetx) -> {
						ClothConfigScreen screen = (ClothConfigScreen) new ConfigBuilder()
								.createConfigScreen(Velvet.velvet.getWrapper().wrapScreen(this))
								.getHandler(Screen.class);
						screen.selectedCategoryIndex = 2;
						this.minecraft.displayGuiScreen((Screen) screen);
					}, new TranslationTextComponent("category.firstperson.cosmetics")));
		}
	}

}
