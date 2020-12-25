package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.tr7zw.firstperson.accessor.PlayerEntityModelAccessor;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

/**
 * Used to expose the thinArms setting of the player model
 *
 */
@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin implements PlayerEntityModelAccessor{

	@Shadow
	private boolean thinArms;
	
	@Override
	public boolean hasThinArms() {
		return thinArms;
	}
	
}
