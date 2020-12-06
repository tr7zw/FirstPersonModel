package de.tr7zw.firstperson.features.head;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.features.Head;
import de.tr7zw.firstperson.util.SettingsUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class ItemHeadFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead>
		extends
			FeatureRenderer<T, M> {
	private final float field_24474;
	private final float field_24475;
	private final float field_24476;

	public ItemHeadFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
		this(featureRendererContext, 1.0F, 1.0F, 1.0F);
	}

	public ItemHeadFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, float f, float g, float h) {
		super(featureRendererContext);
		this.field_24474 = f;
		this.field_24475 = g;
		this.field_24476 = h;
	}
	
	private ItemStack bone = Items.BONE.getStackForRender();
	private ItemStack lead = Items.LEAD.getStackForRender();

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity,
			float f, float g, float h, float j, float k, float l) {
		//ItemStack headItem = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
		ItemStack itemStack = ItemStack.EMPTY;
		if(SettingsUtil.hasEnabled((AbstractClientPlayerEntity) livingEntity, Head.BONE)) {
			itemStack = bone;
		}else if (SettingsUtil.hasEnabled((AbstractClientPlayerEntity) livingEntity, Head.LEAD)) {
			itemStack = lead;
		}
		if (FirstPersonModelMod.isFixActive(livingEntity, matrixStack)) {
			return;
		}
		if (!itemStack.isEmpty()) {
			Item item = itemStack.getItem();
			matrixStack.push();
			matrixStack.scale(this.field_24474, this.field_24475, this.field_24476);
			if (livingEntity.isBaby() && !(livingEntity instanceof VillagerEntity)) {
				matrixStack.translate(0.0D, 0.03125D, 0.0D);
				matrixStack.scale(0.7F, 0.7F, 0.7F);
				matrixStack.translate(0.0D, 1.0D, 0.0D);
			}

			((ModelWithHead) this.getContextModel()).getHead().rotate(matrixStack);
			if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock) {
				matrixStack.scale(1.1875F, -1.1875F, -1.1875F);

				GameProfile gameProfile = null;
				if (itemStack.hasTag()) {
					CompoundTag compoundTag = itemStack.getTag();
					if (compoundTag.contains("SkullOwner", 10)) {
						gameProfile = NbtHelper.toGameProfile(compoundTag.getCompound("SkullOwner"));
					} else if (compoundTag.contains("SkullOwner", 8)) {
						String string = compoundTag.getString("SkullOwner");
						if (!StringUtils.isBlank(string)) {
							gameProfile = SkullBlockEntity.loadProperties(new GameProfile((UUID) null, string));
							compoundTag.put("SkullOwner", NbtHelper.fromGameProfile(new CompoundTag(), gameProfile));
						}
					}
				}

				matrixStack.translate(-0.5D, 0.0D, -0.5D);
				SkullBlockEntityRenderer.render((Direction) null, 180.0F,
						((AbstractSkullBlock) ((BlockItem) item).getBlock()).getSkullType(), gameProfile, f,
						matrixStack, vertexConsumerProvider, i);
			} else if (!(item instanceof ArmorItem) || ((ArmorItem) item).getSlotType() != EquipmentSlot.HEAD) {
				matrixStack.translate(0.0D, -0.25D, 0.0D);
				matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
				matrixStack.scale(0.625F, -0.625F, -0.625F);

				MinecraftClient.getInstance().getHeldItemRenderer().renderItem(livingEntity, itemStack, Mode.HEAD,
						false, matrixStack, vertexConsumerProvider, i);
			}

			matrixStack.pop();
		}
	}
}