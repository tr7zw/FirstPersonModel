//#if MC >= 11800
package dev.tr7zw.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import net.minecraft.SharedConstants;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.Bootstrap;

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
        objenesis.newInstance(ElytraLayer.class);
        objenesis.newInstance(CapeLayer.class);
        objenesis.newInstance(FishingHookRenderer.class);
        objenesis.newInstance(ItemInHandLayer.class);
        objenesis.newInstance(ItemInHandRenderer.class);
        objenesis.newInstance(HumanoidModel.class);
        objenesis.newInstance(PlayerRenderer.class);
        objenesis.newInstance(EntityRenderDispatcher.class);
        objenesis.newInstance(ArrowLayer.class);
        objenesis.newInstance(LevelRenderer.class);
    }

}
//#endif