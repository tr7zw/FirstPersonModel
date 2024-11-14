package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;

//#if MC < 12103
//$$import org.spongepowered.asm.mixin.Shadow;
//$$import dev.tr7zw.firstperson.access.AgeableListModelAccess;
//$$import net.minecraft.client.model.AgeableListModel;
//#endif
import net.minecraft.client.model.geom.ModelPart;

//#if MC >= 12103
@Mixin(ModelPart.class)
public abstract class AgeableListModelMixin {
}
//#else
//$$@Mixin(AgeableListModel.class)
//$$public abstract class AgeableListModelMixin implements AgeableListModelAccess {
//$$
//$$    @Override
//$$    public Iterable<ModelPart> firstPersonHeadPartsGetter() {
//$$        return headParts();
//$$    }
//$$
//$$    @Shadow
//$$    public abstract Iterable<ModelPart> headParts();
//$$
//$$}
//#endif