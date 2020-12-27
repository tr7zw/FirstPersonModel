package dev.tr7zw.firstperson.forge.listener;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class PlayerRendererListener {

	public static void onRenderPre(RenderPlayerEvent.Pre event) {
		//boolean bodyLayer = BodyLayerFeatureRenderer.isEnabled(abstractClientPlayerEntity);
		if (FirstPersonModelCore.instance.isFixActive(event.getPlayer(), event.getMatrixStack())) {
			PlayerModel<AbstractClientPlayerEntity> playerEntityModel_1 = event.getRenderer().getEntityModel();
			playerEntityModel_1.bipedHead.showModel = false;
			((ModelPartBase)playerEntityModel_1.bipedHead).setHidden();
			playerEntityModel_1.bipedHeadwear.showModel = false;
			if (FirstPersonModelMod.config.firstPerson.vanillaHands) {
				playerEntityModel_1.bipedLeftArm.showModel = false;
				playerEntityModel_1.bipedLeftArmwear.showModel = false;
				playerEntityModel_1.bipedRightArm.showModel = false;
				playerEntityModel_1.bipedRightArmwear.showModel = false;
			} else {
				// Do nothing
			}
		} else {
			//playerEntityRenderer.getModel().helmet.showModel = HeadLayerFeatureRenderer
			//		.isEnabled(abstractClientPlayerEntity) ? false : playerEntityRenderer.getModel().helmet.visible;
		}
		/*playerEntityRenderer.getModel().leftSleeve.showModel = bodyLayer ? false
				: playerEntityRenderer.getModel().leftSleeve.showModel;
		playerEntityRenderer.getModel().rightSleeve.showModel = bodyLayer ? false
				: playerEntityRenderer.getModel().rightSleeve.showModel;
		playerEntityRenderer.getModel().leftPantLeg.showModel = bodyLayer ? false
				: playerEntityRenderer.getModel().leftPantLeg.showModel;
		playerEntityRenderer.getModel().rightPantLeg.showModel = bodyLayer ? false
				: playerEntityRenderer.getModel().rightPantLeg.showModel;
		playerEntityRenderer.getModel().jacket.showModel = bodyLayer ? false
				: playerEntityRenderer.getModel().jacket.showModel;*/
	}
	
	public static void onRenderPost(RenderPlayerEvent.Post event) {
		((ModelPartBase)event.getRenderer().getEntityModel().bipedHead).showAgain();
	}
	
}
