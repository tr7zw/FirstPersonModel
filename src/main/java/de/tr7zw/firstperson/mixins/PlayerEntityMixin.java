package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.PlayerSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerSettings{

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private int customHeight = 100;
	private boolean femaleModel = false;
	
	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if(this.getDisplayName().asString().equals("tr7zw")) { // Custom loading here
			customHeight = 110;
			femaleModel = false;
		}
	}

	@Override
	public int getCustomHeight() {
		return customHeight;
	}

	@Override
	public boolean femaleModel() {
		return femaleModel;
	}

}
