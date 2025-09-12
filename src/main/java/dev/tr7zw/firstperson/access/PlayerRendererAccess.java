package dev.tr7zw.firstperson.access;

import java.util.List;

import net.minecraft.client.renderer.entity.layers.RenderLayer;

public interface PlayerRendererAccess {

    List<RenderLayer> getRenderLayers();

    void updatePartsList(boolean thirdperson);

}
