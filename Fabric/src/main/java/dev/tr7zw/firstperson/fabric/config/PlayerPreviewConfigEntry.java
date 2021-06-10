package dev.tr7zw.firstperson.fabric.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.config.CosmeticSettings;
import dev.tr7zw.firstperson.config.SharedConfigBuilder;
import dev.tr7zw.firstperson.features.Back;
import dev.tr7zw.firstperson.features.Boots;
import dev.tr7zw.firstperson.features.Chest;
import dev.tr7zw.firstperson.features.Hat;
import dev.tr7zw.firstperson.features.Head;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

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
	private void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity livingEntity, float delta,
			boolean lockHead) {
		float h = (float) Math.atan((double) (mouseX / 40.0F));
		float l = (float) Math.atan((double) (mouseY / 40.0F));
        MatrixStack matrixStack1 = RenderSystem.getModelViewStack();
        matrixStack1.push();
        matrixStack1.translate((double) x, (double) y, 1050.0D);
        matrixStack1.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0D, 0.0D, 1000.0D);
        matrixStack2.scale((float) size, (float) size, (float) size);
		Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
		Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(l * 20.0F);
		quaternion.hamiltonProduct(quaternion2);
		matrixStack2.multiply(quaternion);
		float m = livingEntity.bodyYaw;
		float prevBodyYaw = livingEntity.prevBodyYaw;
		float n = livingEntity.getYaw();
		float prevYaw = livingEntity.prevYaw;
		float o = livingEntity.getPitch();
		float prevPitch = livingEntity.prevPitch;
		float p = livingEntity.prevHeadYaw;
		float q = livingEntity.headYaw;
		livingEntity.bodyYaw = 180.0F + h * 20.0F;
		livingEntity.setYaw(180.0F + h * 40.0F);
		livingEntity.prevBodyYaw = livingEntity.bodyYaw;
		livingEntity.prevYaw = livingEntity.getYaw();
		if (lockHead) {
			livingEntity.setPitch(-l * 20.0F);
			livingEntity.prevPitch = livingEntity.getPitch();
			livingEntity.headYaw = livingEntity.getYaw();
			livingEntity.prevHeadYaw = livingEntity.getYaw();
		} else {
			livingEntity.headYaw = 180.0F + h * 40.0F - (m - q);
			livingEntity.prevHeadYaw = 180.0F + h * 40.0F - (prevBodyYaw - p);
		}
		CosmeticSettings settings = FirstPersonModelCore.config.cosmetic;
		Hat hat = settings.hat;
		settings.hat = SharedConfigBuilder.hatSelection.getEnumValue(Hat.class);
		Head head = settings.head;
		settings.head = SharedConfigBuilder.headSelection.getEnumValue(Head.class);
		Chest chest = settings.chest;
		settings.chest = SharedConfigBuilder.chestSelection.getEnumValue(Chest.class);
		Back back = settings.back;
		settings.back = SharedConfigBuilder.backSelection.getEnumValue(Back.class);
		Boots boots = settings.boots;
		settings.boots = SharedConfigBuilder.bootsSelection.getEnumValue(Boots.class);
		int playerSize = settings.playerSize;
		settings.playerSize = SharedConfigBuilder.sizeSelection.getIntValue();
		int backHue = settings.backHue;
		settings.backHue = SharedConfigBuilder.backHueSelection.getIntValue();
		boolean allowSizeChange = settings.modifyCameraHeight;
		settings.modifyCameraHeight = true;
		DiffuseLighting.method_34742();
		EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
		quaternion2.conjugate();
		entityRenderDispatcher.setRotation(quaternion2);
		entityRenderDispatcher.setRenderShadows(false);
		VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
		// Mc renders the player in the inventory without delta, causing it to look "laggy". Good luck unseeing this :)
		entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack2, immediate, 15728880);
		immediate.draw();
		entityRenderDispatcher.setRenderShadows(true);
		livingEntity.bodyYaw = m;
		livingEntity.prevBodyYaw = prevBodyYaw;
		livingEntity.setYaw(n);
		livingEntity.prevYaw = prevYaw;
		livingEntity.setPitch(o);
		livingEntity.prevPitch = prevPitch;
		livingEntity.prevHeadYaw = p;
		livingEntity.headYaw = q;
		settings.hat = hat;
		settings.head = head;
		settings.chest = chest;
		settings.back = back;
		settings.boots = boots;
		settings.playerSize = playerSize;
		settings.modifyCameraHeight = allowSizeChange;
		settings.backHue = backHue;
        matrixStack1.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
	}

    @Override
    public List<? extends Selectable> narratables() {
        return new ArrayList();
    }
	
}
