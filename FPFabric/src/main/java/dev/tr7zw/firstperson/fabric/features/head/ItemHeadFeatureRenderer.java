package dev.tr7zw.firstperson.fabric.features.head;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.features.Head;
import dev.tr7zw.firstperson.util.SettingsUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class ItemHeadFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel>
		extends
			RenderLayer<T, M> {
	private final float field_24474;
	private final float field_24475;
	private final float field_24476;

	public ItemHeadFeatureRenderer(RenderLayerParent<T, M> featureRendererContext) {
		this(featureRendererContext, 1.0F, 1.0F, 1.0F);
	}

	public ItemHeadFeatureRenderer(RenderLayerParent<T, M> featureRendererContext, float f, float g, float h) {
		super(featureRendererContext);
		this.field_24474 = f;
		this.field_24475 = g;
		this.field_24476 = h;
	}
	
	private ItemStack bone = Items.BONE.getDefaultInstance();
	private ItemStack lead = Items.LEAD.getDefaultInstance();

	public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity,
			float f, float g, float h, float j, float k, float l) {
		//ItemStack headItem = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
		ItemStack itemStack = ItemStack.EMPTY;
		if(SettingsUtil.hasEnabled((AbstractClientPlayer) livingEntity, Head.BONE)) {
			itemStack = bone;
		}else if (SettingsUtil.hasEnabled((AbstractClientPlayer) livingEntity, Head.LEAD)) {
			itemStack = lead;
		}
		if (FirstPersonModelCore.isRenderingPlayer) {
			return;
		}
		if (!itemStack.isEmpty()) {
			Item item = itemStack.getItem();
			matrixStack.pushPose();
			matrixStack.scale(this.field_24474, this.field_24475, this.field_24476);
			if (livingEntity.isBaby() && !(livingEntity instanceof Villager)) {
				matrixStack.translate(0.0D, 0.03125D, 0.0D);
				matrixStack.scale(0.7F, 0.7F, 0.7F);
				matrixStack.translate(0.0D, 1.0D, 0.0D);
			}

			((HeadedModel) this.getParentModel()).getHead().translateAndRotate(matrixStack);
			if (!(item instanceof ArmorItem) || ((ArmorItem) item).getSlot() != EquipmentSlot.HEAD) {
				matrixStack.translate(0.0D, -0.25D, 0.0D);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
				matrixStack.scale(0.625F, -0.625F, -0.625F);

				Minecraft.getInstance().getItemInHandRenderer().renderItem(livingEntity, itemStack, TransformType.HEAD,
						false, matrixStack, vertexConsumerProvider, i);
			}

			matrixStack.popPose();
		}
	}
}