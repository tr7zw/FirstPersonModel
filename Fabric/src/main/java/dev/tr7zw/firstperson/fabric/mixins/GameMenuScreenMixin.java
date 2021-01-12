package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.fabric.config.ConfigBuilder;
import dev.tr7zw.velvet.api.Velvet;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

	protected GameMenuScreenMixin(Text title) {
		super(title);
	}
	
	private static final Identifier BUTTON_TEXTURE = new Identifier("firstperson", "textures/gui/cosmetics.png");

	@Inject(method = "initWidgets", at = @At("RETURN"))
	public void initWidgets(CallbackInfo info) {
		if (!FirstPersonModelCore.config.firstPerson.hideCosmeticsButton) {
			//(int i, int j, int k, int l, int m, int n, int o, Identifier identifier, int p, int q,
			//		ButtonWidget.PressAction pressAction, Text text)
			this.addButton(new TexturedButtonWidget(this.width / 2 - 122, this.height / 4 + 96 + -16, 20, 20, 0, 0, 20, BUTTON_TEXTURE, 32, 64,
					(buttonWidgetx) -> {
						ClothConfigScreen screen = (ClothConfigScreen) new ConfigBuilder()
								.createConfigScreen(Velvet.velvet.getWrapper().wrapScreen(this))
								.getHandler(Screen.class);
						screen.selectedCategoryIndex = 2;
						this.client.openScreen(screen);
					}, new TranslatableText("category.firstperson.cosmetics")));
		}
	}

}
