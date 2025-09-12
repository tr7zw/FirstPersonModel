package dev.tr7zw.firstperson.modsupport;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.tr7zw.firstperson.api.PlayerOffsetHandler;
import dev.tr7zw.transition.mc.MathUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
//#if MC < 12000
//$$ import net.minecraft.client.Minecraft;
//#endif
//#if MC >= 11903
import org.joml.Matrix4f;
import org.joml.Vector4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//$$ import com.mojang.math.Vector4f;
//#endif

public class PlayerAnimatorSupport implements PlayerOffsetHandler {

    @Override
    public Vec3 applyOffset(AbstractClientPlayer entity, float delta, Vec3 original, Vec3 current) {
        if (entity instanceof IAnimatedPlayer player && player.playerAnimator_getAnimation().isActive()) {
            AnimationProcessor anim = player.playerAnimator_getAnimation();
            anim.setTickDelta(delta); // Probably the tick is done, set tick delta.

            // get required data
            //#if MC >= 12103
            Vec3f rot = anim.get3DTransform(dev.kosmx.playerAnim.api.PartKey.BODY, TransformType.ROTATION, Vec3f.ZERO);
            Vec3f pos = anim.get3DTransform(dev.kosmx.playerAnim.api.PartKey.BODY, TransformType.POSITION, Vec3f.ZERO); // .scale(1 / 16f); It has been
            // pre-scaled
            Vec3f headPos = anim
                    .get3DTransform(dev.kosmx.playerAnim.api.PartKey.HEAD, TransformType.POSITION, Vec3f.ZERO)
                    .scale(1 / 16f);
            //#else
            //$$Vec3f rot = anim.get3DTransform("body", TransformType.ROTATION, Vec3f.ZERO);
            //$$Vec3f pos = anim.get3DTransform("body", TransformType.POSITION, Vec3f.ZERO); // .scale(1 / 16f); It has been
            //$$                                                                             // pre-scaled
            //$$Vec3f headPos = anim.get3DTransform("head", TransformType.POSITION, Vec3f.ZERO).scale(1 / 16f);
            //#endif
            //#if MC >= 11903
            // Matrix4f#translate is an offset applied from LEFT (or absolute)
            Matrix4f relativeTranslation = new Matrix4f();
            relativeTranslation.identity();
            relativeTranslation.scale(-1f); // 0.935 scaling is not even needed :D
            final float bodyOffset = 0.8f; // Distance from the base origin to the head pivot
            relativeTranslation.translate(0, -bodyOffset, 0); // Shift matrix down

            relativeTranslation.translate(pos.getX(), pos.getY(), pos.getZ()); // Apply torso
                                                                               // transformation

            relativeTranslation.rotate(MathUtil.ZP.rotation(rot.getZ()));
            relativeTranslation.rotate(MathUtil.YP.rotation(rot.getY()));
            relativeTranslation.rotate(MathUtil.XP.rotation(rot.getX()));
            // relativeTranslation.multiply(Quaternion.fromXYZ(rot.getX(), rot.getY(),
            // rot.getZ()));

            relativeTranslation.translate(-headPos.getX(), -headPos.getY(), headPos.getZ());

            relativeTranslation.translate(0, bodyOffset, 0); // Roll back the first [0,1,0] translation.

            // calculate the actual rotations and
            float realYaw = Mth.rotLerp(delta, entity.yBodyRotO, entity.yBodyRot);

            Matrix4f matrix = new Matrix4f(); // To multiply from LEFT, I have to create a new instance?!
            matrix.mul(new Matrix4f().scale(-1, 1, 1)); // What is going on with this?!

            matrix.mul(new Matrix4f().rotate(MathUtil.YP.rotationDegrees(realYaw)));

            matrix.mul(new Matrix4f().scale(1, 1, -1));
            matrix.mul(relativeTranslation);

            Vector4f offset = new Vector4f(0, 0, 0, 1);
            offset.mul(matrix);
            //#elseif MC > 11700
            //$$    Matrix4f relativeTranslation = new Matrix4f();
            //$$    relativeTranslation.setIdentity();
            //$$   relativeTranslation.multiply(-1f); // 0.935 scaling is not even needed :D
            //$$    final float bodyOffset = 0.8f; // Distance from the base origin to the head pivot
            //$$     relativeTranslation.multiplyWithTranslation(0, -bodyOffset, 0); // Shift matrix down
            //$$
            //$$   relativeTranslation.multiplyWithTranslation(pos.getX(), pos.getY(), pos.getZ()); // Apply torso
            //$$                                                                                     // transformation
            //$$
            //$$    relativeTranslation.multiply(MathUtil.ZP.rotation(rot.getZ()));
            //$$    relativeTranslation.multiply(MathUtil.YP.rotation(rot.getY()));
            //$$     relativeTranslation.multiply(MathUtil.XP.rotation(rot.getX()));
            //$$     // relativeTranslation.multiply(Quaternion.fromXYZ(rot.getX(), rot.getY(),
            //$$     // rot.getZ()));
            //$$
            //$$     relativeTranslation.multiplyWithTranslation(-headPos.getX(), -headPos.getY(), headPos.getZ());
            //$$
            //$$     relativeTranslation.multiplyWithTranslation(0, bodyOffset, 0); // Roll back the first [0,1,0] translation.
            //$$
            //$$      // calculate the actual rotations and
            //$$     float realYaw = Mth.rotLerp(Minecraft.getInstance().getFrameTime(), entity.yBodyRotO, entity.yBodyRot);
            //$$
            //$$      Matrix4f matrix = new Matrix4f(); // To multiply from LEFT, I have to create a new instance?!
            //$$      matrix.setIdentity();
            //$$      matrix.multiply(Matrix4f.createScaleMatrix(-1, 1, 1)); // What is going on with this?!
            //$$
            //$$       matrix.multiply(new Matrix4f(MathUtil.YP.rotationDegrees(realYaw)));
            //$$
            //$$        matrix.multiply(Matrix4f.createScaleMatrix(1, 1, -1));
            //$$          matrix.multiply(relativeTranslation);
            //$$
            //$$            Vector4f offset = new Vector4f(0, 0, 0, 1);
            //$$           offset.transform(matrix);
            //#else
            //$$  Matrix4f relativeTranslation = new Matrix4f();
            //$$  relativeTranslation.setIdentity();
            //$$ relativeTranslation.multiply(-1f); // 0.935 scaling is not even needed :D
            //$$  final float bodyOffset = 0.8f; // Distance from the base origin to the head pivot
            //$$   relativeTranslation.multiply(Matrix4f.createTranslateMatrix(0, -bodyOffset, 0)); // Shift matrix down
            //$$
            //$$ relativeTranslation.multiply(Matrix4f.createTranslateMatrix(pos.getX(), pos.getY(), pos.getZ())); // Apply torso
            //$$                                                                                    // transformation
            //$$
            //$$  relativeTranslation.multiply(MathUtil.ZP.rotation(rot.getZ()));
            //$$  relativeTranslation.multiply(MathUtil.YP.rotation(rot.getY()));
            //$$   relativeTranslation.multiply(MathUtil.XP.rotation(rot.getX()));
            //$$   // relativeTranslation.multiply(Quaternion.fromXYZ(rot.getX(), rot.getY(),
            //$$   // rot.getZ()));
            //$$
            //$$   relativeTranslation.multiply(Matrix4f.createTranslateMatrix(-headPos.getX(), -headPos.getY(), headPos.getZ()));
            //$$
            //$$  relativeTranslation.multiply(Matrix4f.createTranslateMatrix(0, bodyOffset, 0)); // Roll back the first [0,1,0] translation.
            //$$
            //$$    // calculate the actual rotations and
            //$$    float realYaw = Mth.rotLerp(Minecraft.getInstance().getFrameTime(), entity.yBodyRotO, entity.yBodyRot);
            //$$
            //$$    Matrix4f matrix = new Matrix4f(); // To multiply from LEFT, I have to create a new instance?!
            //$$    matrix.setIdentity();
            //$$    matrix.multiply(Matrix4f.createScaleMatrix(-1, 1, 1)); // What is going on with this?!
            //$$
            //$$     matrix.multiply(new Matrix4f(MathUtil.YP.rotationDegrees(realYaw)));
            //$$
            //$$      matrix.multiply(Matrix4f.createScaleMatrix(1, 1, -1));
            //$$        matrix.multiply(relativeTranslation);
            //$$
            //$$          Vector4f offset = new Vector4f(0, 0, 0, 1);
            //$$          offset.transform(matrix);
            //#endif

            // You may use the Y offset too. new Vector, since it already cancels out all
            // deltas
            return current.add(offset.x(), /* offset.y() */ 0, offset.z());
        }
        return current;
    }

}
