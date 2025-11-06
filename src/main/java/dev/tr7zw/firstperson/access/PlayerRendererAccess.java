package dev.tr7zw.firstperson.access;

import net.minecraft.client.renderer.entity.layers.*;

import java.util.*;

public interface PlayerRendererAccess {

    List<RenderLayer> getRenderLayers();

    void updatePartsList(boolean thirdperson);

}
