package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin{

	@Shadow
	World world;
	@Shadow
	double prevX;
	@Shadow
	double prevY;
	@Shadow
	double prevZ;
	
	@Shadow
	public abstract Vec3d getRotationVec(float tickDelta);
	@Shadow
	public abstract EntityPose getPose();
	@Shadow
	public abstract double getEyeY();
	
	@Shadow
	public abstract double getX();
	@Shadow
	public abstract double getY();
	@Shadow
	public abstract double getZ();
	
	
	@Inject(method = "rayTrace", at = @At("HEAD"), cancellable = true)
	public HitResult rayTrace(double maxDistance, float tickDelta, boolean includeFluids, CallbackInfoReturnable<HitResult> info) {
		if(((Entity)(Object)this) == MinecraftClient.getInstance().player) {
			Vec3d vec3d = this.getVanillaCameraPosVec(tickDelta);
			Vec3d vec3d2 = this.getRotationVec(tickDelta);
			Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
			HitResult res = this.world.rayTrace(new RayTraceContext(vec3d, vec3d3, RayTraceContext.ShapeType.OUTLINE,
					includeFluids ? RayTraceContext.FluidHandling.ANY : RayTraceContext.FluidHandling.NONE, ((Entity)(Object)this)));
			info.setReturnValue(res);
			// Return type is what is selected
		}
		return null;
	}
	
	public final Vec3d getVanillaCameraPosVec(float tickDelta) {
		if (tickDelta == 1.0f) {
			return new Vec3d(this.getX(), this.getEyeY(), this.getZ());
		}
		double d = MathHelper.lerp((double) tickDelta, (double) this.prevX, (double) this.getX());
		double e = MathHelper.lerp((double) tickDelta, (double) this.prevY, (double) this.getY())
				+ (double) this.getVanillaActiveEyeHeight(getPose());
		double f = MathHelper.lerp((double) tickDelta, (double) this.prevZ, (double) this.getZ());
		return new Vec3d(d, e, f);
	}
	
	public float getVanillaActiveEyeHeight(EntityPose pose) {
		switch (pose) {
			case SWIMMING :
			case FALL_FLYING :
			case SPIN_ATTACK : {
				return 0.4f;
			}
			case CROUCHING : {
				return 1.27f;
			}
		}
		return 1.62f;
	}
	
}
