package dev.tr7zw.firstperson.forge.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.tr7zw.firstperson.config.CosmeticSettings;
import de.tr7zw.firstperson.config.SharedConfigBuilder;
import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import me.shedaniel.clothconfig2.forge.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.IntegerSliderEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;

public class PlayerPreviewConfigEntry extends AbstractConfigListEntry<Object> {

	public PlayerPreviewConfigEntry() {
		super(new StringTextComponent(""), false);
	}

	private final Minecraft mc = Minecraft.getInstance();

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
		// int lookSides = -FirstPersonModelMod.config.dollLookingSides;
		// int lookUpDown = FirstPersonModelMod.config.dollLookingUpDown;
		LivingEntity playerEntity = mc.player;
		if (playerEntity == null)
			return;
		float timestep = System.currentTimeMillis() % 8000;
		timestep /= 8000F;
		timestep *= 2 * Math.PI;
		timestep = (float) Math.sin(timestep);
		drawEntity(x + entryWidth / 2, y + entryHeight, entryHeight / 2, timestep * 80f, 10, playerEntity, delta, true);
	}

	@Override
	public int getItemHeight() {
		if (mc.player == null)
			return 0;
		return 100;
	}

	// Modified version from InventoryScreen
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void drawEntity(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity,
			float delta, boolean lockHead) {
		float h = (float) Math.atan((double) (mouseX / 40.0F));
		float l = (float) Math.atan((double) (mouseY / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float) scale, (float) scale, (float) scale);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Vector3f.XP.rotationDegrees(l * 20.0F);
		quaternion.multiply(quaternion1);
		matrixstack.rotate(quaternion);
		float m = livingEntity.rotationYaw;
		float prevBodyYaw = livingEntity.prevRenderYawOffset;
		float n = livingEntity.rotationYaw;
		float prevYaw = livingEntity.prevRotationYaw;
		float o = livingEntity.rotationPitch;
		float prevPitch = livingEntity.prevRotationPitch;
		float p = livingEntity.prevRotationYawHead;
		float q = livingEntity.rotationYawHead;
		livingEntity.renderYawOffset = 180.0F + h * 20.0F;
		livingEntity.rotationYawHead = 180.0F + h * 40.0F;
		livingEntity.prevRenderYawOffset = livingEntity.renderYawOffset;
		livingEntity.prevRotationYawHead = livingEntity.rotationYawHead;
		if (lockHead) {
			livingEntity.rotationPitch = -l * 20.0F;
			livingEntity.prevRotationPitch = livingEntity.rotationPitch;
			livingEntity.rotationYawHead = livingEntity.rotationYaw;
			livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
		} else {
			livingEntity.rotationYawHead = 180.0F + h * 40.0F - (m - q);
			livingEntity.prevRotationYawHead = 180.0F + h * 40.0F - (prevBodyYaw - p);
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
		settings.playerSize = ((IntegerSliderEntry) SharedConfigBuilder.sizeSelection).getValue();
		boolean allowSizeChange = settings.modifyCameraHeight;
		settings.modifyCameraHeight = true;
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		quaternion1.conjugate();
		entityrenderermanager.setCameraOrientation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers()
				.getBufferSource();
		// Mc renders the player in the inventory without delta, causing it to look
		// "laggy". Good luck unseeing this :)
		RenderSystem.runAsFancy(() -> {
			entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixstack,
					irendertypebuffer$impl, 15728880);
		});
		irendertypebuffer$impl.finish();
		entityrenderermanager.setRenderShadow(true);
		livingEntity.renderYawOffset = m;
		livingEntity.prevRenderYawOffset = prevBodyYaw;
		livingEntity.rotationYaw = n;
		livingEntity.prevRotationYaw = prevYaw;
		livingEntity.rotationPitch = o;
		livingEntity.prevRotationPitch = prevPitch;
		livingEntity.prevRotationYawHead = p;
		livingEntity.rotationYawHead = q;
		settings.hat = hat;
		settings.head = head;
		settings.chest = chest;
		settings.back = back;
		settings.boots = boots;
		settings.playerSize = size;
		settings.modifyCameraHeight = allowSizeChange;
		RenderSystem.popMatrix();
	}

	@Override
	public List<? extends IGuiEventListener> getEventListeners() {
		return new ArrayList<>();
	}

}
