package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Inject(at = @At("HEAD"), method = "render")
	public void render(Entity entity_1, float float_1, boolean boolean_1, CallbackInfo info) {
		if(MinecraftClient.getInstance().options.perspective != 0)return;
		if(entity_1 instanceof AbstractClientPlayerEntity) {
			if(!((PlayerEntity) entity_1).isMainPlayer())return;
			FirstPersonModelMod.hideNextHeadArmor = true;
			FirstPersonModelMod.hideNextHeadItem = true;
			FirstPersonModelMod.isRenderingPlayer = true;
		}
	}

}
