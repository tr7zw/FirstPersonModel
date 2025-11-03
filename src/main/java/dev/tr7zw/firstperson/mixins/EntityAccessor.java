package dev.tr7zw.firstperson.mixins;

import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Entity.class)
public interface EntityAccessor {

    //? if >= 1.21.9 {

    @Accessor("position")
    Vec3 entityCulling$getRawPosition();

    @Accessor("position")
    void entityCulling$setRawPosition(Vec3 position);

    //? }

}
