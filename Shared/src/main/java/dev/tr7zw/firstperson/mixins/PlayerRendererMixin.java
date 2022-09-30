package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.math.Matrix3f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Offset the player behind the camera
 * 
 * @author tr7zw
 *
 */
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    private static Minecraft fpm_mc = Minecraft.getInstance();

    @Inject(method = "getRenderOffset", at = @At("RETURN"), cancellable = true)
    public void getRenderOffset(AbstractClientPlayer entity, float f, CallbackInfoReturnable<Vec3> ci) {
        if (entity == fpm_mc.cameraEntity) {
            FirstPersonModelCore.getWrapper().updatePositionOffset(entity, Vec3.ZERO);

            if (entity instanceof IAnimatedPlayer player && player.getAnimation().isActive()) {
                AnimationProcessor anim = player.getAnimation();

                // get required data
                Vec3f rot = anim.get3DTransform("body", TransformType.ROTATION, Vec3f.ZERO);
                Vec3f pos = anim.get3DTransform("body", TransformType.POSITION, Vec3f.ZERO).scale(1 / 16f);
                Vec3f headPos = anim.get3DTransform("head", TransformType.POSITION, Vec3f.ZERO).scale(1 / 16f);
                
                // Minecrafts vec classes are a mess. Calculate the 
                Vector3f rotationVector = new Vector3f(0,1f,0);
                rotationVector.transform(Quaternion.fromXYZ(rot.getX(), rot.getY(), rot.getZ()));
                Vec3f rotVec = new Vec3f(rotationVector.x(), rotationVector.y(), rotationVector.z());
                Vec3f offset = pos./*add(rotVec).*/add(headPos).scale(0.9f);
                
                // calculate the actual rotations and 
                double realYaw = Mth.rotLerp(fpm_mc.getFrameTime(), entity.yBodyRotO, entity.yBodyRot);
                double ca = Math.sin(Math.toRadians(realYaw));
                double sa = Math.cos(Math.toRadians(realYaw));
                double offsetX = ca * offset.getX() - sa * offset.getZ();
                double offsetZ = sa * offset.getX() + ca * offset.getZ();
                ci.setReturnValue(ci.getReturnValue().add(FirstPersonModelCore.getWrapper().getOffset()).add(-offsetX, 0,
                        offsetZ));
            }

            ci.setReturnValue(ci.getReturnValue().add(FirstPersonModelCore.getWrapper().getOffset()));
        }
    }

}
