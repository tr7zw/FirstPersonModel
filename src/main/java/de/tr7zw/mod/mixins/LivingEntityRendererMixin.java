package de.tr7zw.mod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	protected LivingEntityRendererMixin(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1);
		// TODO Auto-generated constructor stub
	}

	@Inject(at = @At("HEAD"), method = "method_4055", cancellable=true)
	private boolean nameplateRender(T livingEntity_1, CallbackInfoReturnable<Boolean> info) {
		if(livingEntity_1 == MinecraftClient.getInstance().player)
			info.setReturnValue(true);
		return true;
	}
	
}
