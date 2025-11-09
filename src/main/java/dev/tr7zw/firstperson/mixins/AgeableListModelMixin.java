package dev.tr7zw.firstperson.mixins;

import net.minecraft.client.model.geom.*;
import org.spongepowered.asm.mixin.*;

//? if >= 1.21.3 {

@Mixin(ModelPart.class)
public abstract class AgeableListModelMixin {
}
//? } else {
/*import dev.tr7zw.firstperson.access.*;
import net.minecraft.client.model.*;

@Mixin(AgeableListModel.class)
public abstract class AgeableListModelMixin implements AgeableListModelAccess {

    @Override
    public Iterable<ModelPart> firstPersonHeadPartsGetter() {
        return headParts();
    }

    @Shadow
    public abstract Iterable<ModelPart> headParts();

}
*///? }
