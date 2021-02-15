package dev.tr7zw.firstperson.forge.mixin;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.mixinbase.ClientPlayNetworkHandlerBase;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.play.server.SEntityMetadataPacket;

//ClientPlayNetworkHandler
@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetHandlerMixin implements ClientPlayNetworkHandlerBase{

	@Shadow
	private ClientWorld world;

	@Inject(method = "handleEntityMetadata", at = @At("HEAD"))
	public void handleEntityMetadata(SEntityMetadataPacket packet, CallbackInfo callback) {
		handle(world != null ? transliteration.singletonWrapper().getWorld().of(world) : null, transliteration.getWrapper().wrapEntityTrackerUpdatePacket(packet));
	}
	
}
