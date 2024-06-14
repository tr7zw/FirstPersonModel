//#if FORGE
//$$package dev.tr7zw.firstperson;
//$$
//$$import net.minecraftforge.api.distmarker.Dist;
//$$import net.minecraftforge.fml.DistExecutor;
//$$import net.minecraftforge.fml.common.Mod;
//$$
//$$@Mod("firstperson")
//$$public class FirstPersonBootstrap {
//$$
//$$    public FirstPersonBootstrap() {
//$$            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> FirstPersonModelMod::new);
//$$    }
//$$    
//$$}
//#elseif NEOFORGE
//$$package dev.tr7zw.firstperson;
//$$
//$$import net.neoforged.api.distmarker.Dist;
//$$import net.neoforged.fml.loading.FMLEnvironment;
//$$import net.neoforged.fml.common.Mod;
//$$
//$$@Mod("firstperson")
//$$public class FirstPersonBootstrap {
//$$
//$$    public FirstPersonBootstrap() {
//$$            if(FMLEnvironment.dist == Dist.CLIENT) {
//$$                new FirstPersonModelMod();
//$$            }
//$$    }
//$$    
//$$}
//#endif