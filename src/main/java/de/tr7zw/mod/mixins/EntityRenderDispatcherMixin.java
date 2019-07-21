package de.tr7zw.mod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.VisibleRegion;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	
	@Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
	private void shouldRender(Entity entity_1, VisibleRegion visibleRegion_1, double double_1, double double_2, double double_3, CallbackInfoReturnable<Boolean> info) {

	}
	

	
}
