package dev.tr7zw.firstperson.api;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec3;

public interface PlayerOffsetHandler {

    /**
     * Allows custom model offsets during rendering. The current value is the value
     * pre-calculated by the mod(or handlers running before). If the handler doesn't
     * apply to the current situation, the expected behavior is to just return
     * "current".
     * 
     * @param entity
     * @param delta
     * @param original
     * @param current
     * @return
     */
    Vec3 applyOffset(AbstractClientPlayer entity, float delta, Vec3 original, Vec3 current);

}
