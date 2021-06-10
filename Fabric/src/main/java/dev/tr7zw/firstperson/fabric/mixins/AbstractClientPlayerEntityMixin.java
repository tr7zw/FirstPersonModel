package dev.tr7zw.firstperson.fabric.mixins;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	private Identifier lastId = null;
	private String modelOverwrite = null;

	/*@Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
	public Identifier getSkinTexture(CallbackInfoReturnable<Identifier> info) {
		if(!FirstPersonModelCore.config.firstPerson.playerHeadSkins) {
			if(lastId != null) {
				lastId = null;
				modelOverwrite = null;
				clearSettings();
			}
			return null;
		}
		ItemStack itemStack = getEquippedStack(EquipmentSlot.HEAD);
		Item item = itemStack.getItem();
		if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock) {
			GameProfile gameProfile = null;
			if (itemStack.hasTag()) {
				String string;
				NbtCompound compoundTag = itemStack.getTag();
				if (compoundTag.contains("SkullOwner", 10)) {
					gameProfile = NbtHelper.toGameProfile((NbtCompound) compoundTag.getCompound("SkullOwner"));
				} else if (compoundTag.contains("SkullOwner", 8)
						&& !StringUtils.isBlank((CharSequence) (string = compoundTag.getString("SkullOwner")))) {
					gameProfile = SkullBlockEntity.loadProperties((GameProfile) new GameProfile(null, string));
					compoundTag.put("SkullOwner", (NbtElement) NbtHelper.writeGameProfile((NbtCompound) new NbtCompound(),
							(GameProfile) gameProfile));
				}
				Identifier id = getSkin(gameProfile);
				if(!id.equals(lastId)) {
					lastId = id;
					clearSettings();
				}
				info.setReturnValue(id);
				return id;
			}
		}
		if(lastId != null) {
			lastId = null;
			modelOverwrite = null;
			clearSettings();
		}
		return null;
	}*/
	
	@Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
	public String getModel(CallbackInfoReturnable<String> info) {
		if(modelOverwrite != null) {
			info.setReturnValue(modelOverwrite);
		}
		return null;
	}

	private Identifier getSkin(GameProfile gameProfile) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		Map map = minecraftClient.getSkinProvider().getTextures(gameProfile);
		if (map.containsKey((Object) MinecraftProfileTexture.Type.SKIN)) {
			this.modelOverwrite = ((MinecraftProfileTexture) map.get((Object) MinecraftProfileTexture.Type.SKIN)).getMetadata("model");
			if (this.modelOverwrite == null) {
				this.modelOverwrite = "default";
			}
			return (Identifier) minecraftClient.getSkinProvider().loadSkin(
					(MinecraftProfileTexture) map.get((Object) MinecraftProfileTexture.Type.SKIN),
					MinecraftProfileTexture.Type.SKIN);
		}
		return (Identifier) DefaultSkinHelper
				.getTexture((UUID) PlayerEntity.getUuidFromProfile((GameProfile) gameProfile));
	}
	
	private void clearSettings() {
		((PlayerSettings)this).setupHeadLayers(null);
		((PlayerSettings)this).setupSkinLayers(null);
	}

}
