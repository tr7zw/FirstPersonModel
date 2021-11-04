package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.tr7zw.firstperson.accessor.PlayerEntityModelAccessor;
import net.minecraft.client.model.PlayerModel;

/**
 * Used to expose the thinArms setting of the player model
 *
 */
@Mixin(PlayerModel.class)
public class PlayerEntityModelMixin implements PlayerEntityModelAccessor{

	@Shadow
	private boolean slim;
	
	@Override
	public boolean hasThinArms() {
		return slim;
	}
	
}
