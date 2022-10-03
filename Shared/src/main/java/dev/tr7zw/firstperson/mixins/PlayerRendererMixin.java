package dev.tr7zw.firstperson.mixins;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        if (entity == fpm_mc.cameraEntity && FirstPersonModelCore.enabled) {
            FirstPersonModelCore.getWrapper().updatePositionOffset(entity, Vec3.ZERO);

            if (entity instanceof IAnimatedPlayer player && player.getAnimation().isActive()) {
                AnimationProcessor anim = player.getAnimation();
                anim.setTickDelta(f); //Probably the tick is done, set tick delta.

                // get required data
                Vec3f rot = anim.get3DTransform("body", TransformType.ROTATION, Vec3f.ZERO);
                Vec3f pos = anim.get3DTransform("body", TransformType.POSITION, Vec3f.ZERO); //.scale(1 / 16f); It has been pre-scaled
                Vec3f headPos = anim.get3DTransform("head", TransformType.POSITION, Vec3f.ZERO).scale(1 / 16f);


                //Matrix4f#translate is an offset applied from LEFT (or absolute)
                Matrix4f relativeTranslation = new Matrix4f();
                relativeTranslation.setIdentity();
                relativeTranslation.multiply(-1f); //0.935 scaling is not even needed :D
                final float bodyOffset = 0.8f; //Distance from the base origin to the head pivot
                relativeTranslation.multiplyWithTranslation(0, -bodyOffset, 0); //Shift matrix down


                relativeTranslation.multiplyWithTranslation(pos.getX(), pos.getY(), pos.getZ()); //Apply torso transformation

                relativeTranslation.multiply(Vector3f.ZP.rotation(rot.getZ()));
                relativeTranslation.multiply(Vector3f.YP.rotation(rot.getY()));
                relativeTranslation.multiply(Vector3f.XP.rotation(rot.getX()));
                //relativeTranslation.multiply(Quaternion.fromXYZ(rot.getX(), rot.getY(), rot.getZ()));

                relativeTranslation.multiplyWithTranslation(-headPos.getX(), -headPos.getY(), headPos.getZ());

                relativeTranslation.multiplyWithTranslation(0, bodyOffset, 0); //Roll back the first [0,1,0] translation.


                // calculate the actual rotations and 
                float realYaw = Mth.rotLerp(fpm_mc.getFrameTime(), entity.yBodyRotO, entity.yBodyRot);

                Matrix4f matrix = new Matrix4f(); // To multiply from LEFT, I have to create a new instance?!
                matrix.setIdentity();
                matrix.multiply(Matrix4f.createScaleMatrix(-1, 1, 1)); //What is going on with this?!

                matrix.multiply(new Matrix4f(Vector3f.YP.rotationDegrees(realYaw)));

                matrix.multiply(Matrix4f.createScaleMatrix(1, 1, -1));
                matrix.multiply(relativeTranslation);

                Vector4f offset = new Vector4f(0, 0, 0, 1);
                offset.transform(matrix);

                ci.setReturnValue(ci.getReturnValue().add(FirstPersonModelCore.getWrapper().getOffset()).add(offset.x(), /*offset.y()*/ 0, offset.z())); //You may use the Y offset too
            }

            ci.setReturnValue(ci.getReturnValue().add(FirstPersonModelCore.getWrapper().getOffset()));
        }
    }

}
