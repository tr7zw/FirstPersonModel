package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.versionless.mixinbase.*;
import net.minecraft.client.model.geom.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ModelPart.class)
public class ModelPartMixin implements ModelPartBase {

    @Shadow
    public float z;
    @Shadow
    public boolean visible;

    private float zCopy = 0;
    private boolean moved = false;

    @Override
    public void setHidden() {
        if (!moved) {
            zCopy = z;
        }
        z = 5000;
        moved = true;
        visible = false;
    }

    @Override
    public void showAgain() {
        if (moved) {
            z = zCopy;
            moved = false;
            visible = true;
        }
    }

    @Inject(method = "setPos", at = @At("RETURN"))
    public void setPivot(float x, float y, float z, CallbackInfo info) {
        if (moved) {
            zCopy = z;
            this.z = 5000;
        }
    }

    //? if < 1.21.9 {
    /*@Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyTransform(ModelPart modelPart, CallbackInfo info) {
        if (moved) {
            zCopy = z;
            z = 5000;
        }
    }
    *///? }

    @Override
    public boolean isHidden() {
        return moved;
    }

}
