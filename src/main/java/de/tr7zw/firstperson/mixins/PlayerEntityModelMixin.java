package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.tr7zw.firstperson.PlayerEntityModelAccessor;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin implements PlayerEntityModelAccessor{

	@Shadow
	private boolean thinArms;
	
	@Override
	public boolean hasThinArms() {
		return thinArms;
	}
	
}
