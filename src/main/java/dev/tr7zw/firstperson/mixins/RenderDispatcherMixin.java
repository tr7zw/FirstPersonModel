package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import lombok.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.world.entity.*;

//? if < 1.21.3 {
/*import org.spongepowered.asm.mixin.injection.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
*///? }

/**
 * Disable hitbox rendering for the player in first person mode
 *
 */
@Mixin(EntityRenderDispatcher.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RenderDispatcherMixin {

    private static Minecraft fpmMcInstance = Minecraft.getInstance();

    //? if >= 1.21.9 {

    //? } else if >= 1.21.3 {
    /*
    
    @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
    private static void renderHitbox(PoseStack poseStack, VertexConsumer buffer,
            //? if >= 1.21.5 {
    /^
            HitboxRenderState hitboxRenderState,
            ^///? } else {
    
             Entity entity, float red, float green, float blue, float alpha, 
            //? }
            CallbackInfo ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            ci.cancel();
        }
    }
    
    *///? } else {
    /*
    
    @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
    //? if < 1.17.0 {
    /^
       private void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f, CallbackInfo ci) {
     ^///? } else if < 1.21.0 {
    /^
    private static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f,
            CallbackInfo ci) {
        ^///? } else {
        
          private static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f,
            float g, float h, float i,
                    CallbackInfo ci) {
         //? }
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            ci.cancel();
        }
    }
    *///? }

}
