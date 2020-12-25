package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.tr7zw.firstperson.accessor.PlayerEntityModelAccessor;
import net.minecraft.client.renderer.entity.model.PlayerModel;

@Mixin(PlayerModel.class)
public class PlayerModelMixin implements PlayerEntityModelAccessor{

	@Shadow
	private boolean smallArms;
	
	@Override
	public boolean hasThinArms() {
		return smallArms;
	}

}
