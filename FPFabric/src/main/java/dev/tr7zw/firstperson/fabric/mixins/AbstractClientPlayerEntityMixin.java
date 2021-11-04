package dev.tr7zw.firstperson.fabric.mixins;

import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player {

	public AbstractClientPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	private ResourceLocation lastId = null;
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
	
	@Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
	public String getModel(CallbackInfoReturnable<String> info) {
		if(modelOverwrite != null) {
			info.setReturnValue(modelOverwrite);
		}
		return null;
	}

	private ResourceLocation getSkin(GameProfile gameProfile) {
		Minecraft minecraftClient = Minecraft.getInstance();
		Map map = minecraftClient.getSkinManager().getInsecureSkinInformation(gameProfile);
		if (map.containsKey((Object) MinecraftProfileTexture.Type.SKIN)) {
			this.modelOverwrite = ((MinecraftProfileTexture) map.get((Object) MinecraftProfileTexture.Type.SKIN)).getMetadata("model");
			if (this.modelOverwrite == null) {
				this.modelOverwrite = "default";
			}
			return (ResourceLocation) minecraftClient.getSkinManager().registerTexture(
					(MinecraftProfileTexture) map.get((Object) MinecraftProfileTexture.Type.SKIN),
					MinecraftProfileTexture.Type.SKIN);
		}
		return (ResourceLocation) DefaultPlayerSkin
				.getDefaultSkin((UUID) Player.createPlayerUUID((GameProfile) gameProfile));
	}

}
