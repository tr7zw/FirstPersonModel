//? if >= 1.18.0 {

package dev.tr7zw.tests;

import dev.tr7zw.firstperson.versionless.mixinbase.*;
import net.minecraft.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.server.*;
import org.junit.jupiter.api.*;
import org.objenesis.*;

import static org.junit.jupiter.api.Assertions.*;

public class MixinTests {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void testInjectedInterfaces() {
        Objenesis objenesis = new ObjenesisStd();
        assertTrue((Object) objenesis.newInstance(ModelPart.class) instanceof ModelPartBase);
    }

    @Test
    void testMixins() {
        Objenesis objenesis = new ObjenesisStd();
        objenesis.newInstance(HumanoidArmorLayer.class);
        //        objenesis.newInstance(ElytraLayer.class);
        objenesis.newInstance(CapeLayer.class);
        objenesis.newInstance(FishingHookRenderer.class);
        objenesis.newInstance(ItemInHandLayer.class);
        objenesis.newInstance(ItemInHandRenderer.class);
        objenesis.newInstance(HumanoidModel.class);
        //? if >= 1.21.9 {
        objenesis.newInstance(net.minecraft.client.renderer.entity.player.AvatarRenderer.class);
        //? } else {
        /*
        objenesis.newInstance(net.minecraft.client.renderer.entity.player.PlayerRenderer.class);
        *///? }
        objenesis.newInstance(EntityRenderDispatcher.class);
        objenesis.newInstance(ArrowLayer.class);
        objenesis.newInstance(LevelRenderer.class);
    }

}
//? }
