package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Inject(at = @At("HEAD"), method = "render")
	public <E extends Entity> E render(E entity_1, double double_1, double double_2, double double_3, float float_1, float float_2, MatrixStack matrixStack_1, VertexConsumerProvider vertexConsumerProvider_1, int int_1, CallbackInfo info) {
		if(MinecraftClient.getInstance().options.perspective != 0)return null;
		if(entity_1 instanceof AbstractClientPlayerEntity) {
			if(!((PlayerEntity) entity_1).isMainPlayer())return null;
			FirstPersonModelMod.hideNextHeadArmor = true;
			FirstPersonModelMod.hideNextHeadItem = true;
			FirstPersonModelMod.isRenderingPlayer = true;
		}
		return null;
	}

}
