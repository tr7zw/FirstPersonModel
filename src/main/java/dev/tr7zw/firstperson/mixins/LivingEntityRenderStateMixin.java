package dev.tr7zw.firstperson.mixins;

//? if >= 1.21.9 {
import dev.tr7zw.firstperson.access.*;
import net.minecraft.client.renderer.entity.state.*;
import org.spongepowered.asm.mixin.*;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateAccess {

    @Unique
    private boolean cameraEntity;
    @Unique
    private boolean hideLeftArm;
    @Unique
    private boolean hideRightArm;
    @Unique
    private boolean hideBody;
    @Unique
    private float armOffset;

    @Override
    public void setIsCameraEntity(boolean value) {
        this.cameraEntity = value;
    }

    @Override
    public boolean isCameraEntity() {
        return this.cameraEntity;
    }

    @Override
    public void setHideLeftArm(boolean value) {
        this.hideLeftArm = value;
    }

    @Override
    public boolean hideLeftArm() {
        return this.hideLeftArm;
    }

    @Override
    public void setHideRightArm(boolean value) {
        this.hideRightArm = value;
    }

    @Override
    public boolean hideRightArm() {
        return this.hideRightArm;
    }

    @Override
    public void setHideBody(boolean value) {
        this.hideBody = value;
    }

    @Override
    public boolean hideBody() {
        return this.hideBody;
    }

    @Override
    public void setArmOffset(float offset) {
        this.armOffset = offset;
    }

    @Override
    public float getArmOffset() {
        return this.armOffset;
    }
}
//? } else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public class LivingEntityRenderStateMixin {
}

*///? }
