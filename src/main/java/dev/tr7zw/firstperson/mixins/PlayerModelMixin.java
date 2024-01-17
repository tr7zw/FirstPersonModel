package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.tr7zw.firstperson.access.PlayerModelAccess;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

@Mixin(value = PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> implements PlayerModelAccess {

    @Shadow
    @Final
    private ModelPart cloak;

    public PlayerModelMixin(ModelPart modelPart) {
        // spotless:off
	    //#if MC >= 11700
        super(modelPart);
        //#else
        //$$ super(0);
        //#endif
        //spotless:on
    }

    @Override
    public ModelPart getCloak() {
        return cloak;
    }

}
