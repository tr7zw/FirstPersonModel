package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.access.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
//? if < 1.21.3 {
/*
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.geom.ModelPart;
*///? }

@Mixin(value = PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> implements PlayerModelAccess {

    //? if < 1.21.3 {
    /*
    @Shadow
    @Final
    private ModelPart cloak;
    
    @Override
    public ModelPart getCloak() {
        return cloak;
    }
    *///? }

}
