//? if forge {

// package dev.tr7zw.firstperson;
//
// import net.minecraftforge.api.distmarker.Dist;
// import net.minecraftforge.fml.DistExecutor;
// import net.minecraftforge.fml.common.Mod;
// import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
// import dev.tr7zw.transition.loader.ModLoaderUtil;
//
// @Mod("firstperson")
// public class FirstPersonBootstrap {
//
//    public FirstPersonBootstrap(FMLJavaModLoadingContext context) {
//        ModLoaderUtil.setModLoadingContext(context);
//            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> { 
//         new FirstPersonModelMod().sharedSetup();
//        });
//    }
//    public FirstPersonBootstrap() {
//        this(FMLJavaModLoadingContext.get());
//    }
//    
// }
//? } else if neoforge {

// package dev.tr7zw.firstperson;
//
// import net.neoforged.api.distmarker.Dist;
// import net.neoforged.fml.loading.FMLEnvironment;
// import net.neoforged.fml.common.Mod;
//
// @Mod("firstperson")
// public class FirstPersonBootstrap {
//
//    public FirstPersonBootstrap() {
//            if(FMLEnvironment.dist == Dist.CLIENT) {
//                dev.tr7zw.transition.loader.ModLoaderEventUtil.registerClientSetupListener(() -> new FirstPersonModelMod().sharedSetup());
//            }
//    }
//    
// }
//? }
