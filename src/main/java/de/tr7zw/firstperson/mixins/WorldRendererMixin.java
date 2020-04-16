package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderEntity")
	private void renderEntity(Entity entity_1, double double_1, double double_2, double double_3, float float_1, MatrixStack matrixStack_1, VertexConsumerProvider vertexConsumerProvider_1, CallbackInfo info) {
		if (FirstPersonModelMod.hideNextHeadArmor) FirstPersonModelMod.hideNextHeadArmor = false;
		if (FirstPersonModelMod.hideNextHeadItem) FirstPersonModelMod.hideNextHeadArmor = true; 	//if I don't wear any armor head then another helmet will be hidden without this
		
		/*if (!FirstPersonModelMod.matrix.equals( matrixStack_1)){
			FirstPersonModelMod.matrix = matrixStack_1;
			if (FirstPersonModelMod.debug.length() != 0){
				System.out.println(FirstPersonModelMod.debug);
				FirstPersonModelMod.debug = "";
			}	//figure out what optifine does
		}*/

		if(MinecraftClient.getInstance().options.perspective != 0)return;
		if(entity_1 instanceof AbstractClientPlayerEntity) {
			if(!((PlayerEntity) entity_1).isMainPlayer())return;
			//FirstPersonModelMod.hideNextHeadArmor = true;
			//FirstPersonModelMod.hideNextHeadItem = true;
			FirstPersonModelMod.isRenderingPlayer = true;
			//FirstPersonModelMod.debug = FirstPersonModelMod.debug + "X";
		}
		else{
			//FirstPersonModelMod.debug = FirstPersonModelMod.debug + "I";
		}
	}
	
}
