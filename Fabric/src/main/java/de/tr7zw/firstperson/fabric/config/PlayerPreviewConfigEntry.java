package de.tr7zw.firstperson.fabric.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;

import de.tr7zw.firstperson.config.CosmeticSettings;
import de.tr7zw.firstperson.config.SharedConfigBuilder;
import de.tr7zw.firstperson.fabric.FirstPersonModelMod;
import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Quaternion;

public class PlayerPreviewConfigEntry extends AbstractConfigListEntry<Object> {

	public PlayerPreviewConfigEntry() {
		super(new LiteralText(""), false);
	}
	
	private final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public List<? extends Element> children() {
		return new ArrayList<>();
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public Optional<Object> getDefaultValue() {
		return Optional.of(null);
	}

	@Override
	public void save() {
		
	}

	@Override
	public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
			int mouseY, boolean isHovered, float delta) {
		//int lookSides = -FirstPersonModelMod.config.dollLookingSides;
		//int lookUpDown = FirstPersonModelMod.config.dollLookingUpDown;
		LivingEntity playerEntity = mc.player;
		if(playerEntity == null)return;
		float timestep = System.currentTimeMillis()%8000;
		timestep /= 8000F;
		timestep *= 2*Math.PI;
		timestep = (float) Math.sin(timestep);
		drawEntity(x + entryWidth/2, y + entryHeight, entryHeight/2, timestep * 80f, 10, playerEntity, delta,
				true);
	}

	@Override
	public int getItemHeight() {
		if(mc.player == null)return 0;
		return 100;
	}

	// Modified version from InventoryScreen
	@SuppressWarnings("unchecked")
	private void drawEntity(int i, int j, int k, float f, float g, LivingEntity livingEntity, float delta,
			boolean lockHead) {
		float h = (float) Math.atan((double) (f / 40.0F));
		float l = (float) Math.atan((double) (g / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) i, (float) j, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixStack = new MatrixStack();
		FirstPersonModelMod.paperDollStack = matrixStack; // To not hide head if rendering this
		matrixStack.translate(0.0D, 0.0D, 1000.0D);
		matrixStack.scale((float) k, (float) k, (float) k);
		Quaternion quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
		Quaternion quaternion2 = Vector3f.POSITIVE_X.getDegreesQuaternion(l * 20.0F);
		quaternion.hamiltonProduct(quaternion2);
		matrixStack.multiply(quaternion);
		float m = livingEntity.bodyYaw;
		float prevBodyYaw = livingEntity.prevBodyYaw;
		float n = livingEntity.yaw;
		float prevYaw = livingEntity.prevYaw;
		float o = livingEntity.pitch;
		float prevPitch = livingEntity.prevPitch;
		float p = livingEntity.prevHeadYaw;
		float q = livingEntity.headYaw;
		livingEntity.bodyYaw = 180.0F + h * 20.0F;
		livingEntity.yaw = 180.0F + h * 40.0F;
		livingEntity.prevBodyYaw = livingEntity.bodyYaw;
		livingEntity.prevYaw = livingEntity.yaw;
		if (lockHead) {
			livingEntity.pitch = -l * 20.0F;
			livingEntity.prevPitch = livingEntity.pitch;
			livingEntity.headYaw = livingEntity.yaw;
			livingEntity.prevHeadYaw = livingEntity.yaw;
		} else {
			livingEntity.headYaw = 180.0F + h * 40.0F - (m - q);
			livingEntity.prevHeadYaw = 180.0F + h * 40.0F - (prevBodyYaw - p);
		}
		CosmeticSettings settings = FirstPersonModelMod.config.cosmetic;
		Hat hat = settings.hat;
		settings.hat = ((EnumListEntry<Hat>) SharedConfigBuilder.hatSelection).getValue();
		Head head = settings.head;
		settings.head = ((EnumListEntry<Head>) SharedConfigBuilder.headSelection).getValue();
		Chest chest = settings.chest;
		settings.chest = ((EnumListEntry<Chest>) SharedConfigBuilder.chestSelection).getValue();
		Back back = settings.back;
		settings.back = ((EnumListEntry<Back>) SharedConfigBuilder.backSelection).getValue();
		Boots boots = settings.boots;
		settings.boots = ((EnumListEntry<Boots>) SharedConfigBuilder.bootsSelection).getValue();
		int size = settings.playerSize;
		settings.playerSize = ((IntegerSliderEntry) FirstPersonModMenu.sizeSelection).getValue();
		int backHue = settings.backHue;
		settings.backHue = ((IntegerSliderEntry) FirstPersonModMenu.backHueSelection).getValue();
		boolean allowSizeChange = settings.modifyCameraHeight;
		settings.modifyCameraHeight = true;
		EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
		quaternion2.conjugate();
		entityRenderDispatcher.setRotation(quaternion2);
		entityRenderDispatcher.setRenderShadows(false);
		VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
		// Mc renders the player in the inventory without delta, causing it to look "laggy". Good luck unseeing this :)
		entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack, immediate, 15728880);
		immediate.draw();
		entityRenderDispatcher.setRenderShadows(true);
		livingEntity.bodyYaw = m;
		livingEntity.prevBodyYaw = prevBodyYaw;
		livingEntity.yaw = n;
		livingEntity.prevYaw = prevYaw;
		livingEntity.pitch = o;
		livingEntity.prevPitch = prevPitch;
		livingEntity.prevHeadYaw = p;
		livingEntity.headYaw = q;
		settings.hat = hat;
		settings.head = head;
		settings.chest = chest;
		settings.back = back;
		settings.boots = boots;
		settings.playerSize = size;
		settings.modifyCameraHeight = allowSizeChange;
		settings.backHue = backHue;
		RenderSystem.popMatrix();
	}
	
}
