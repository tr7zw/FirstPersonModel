package dev.tr7zw.firstperson.api;

import java.util.ArrayList;
import java.util.List;

import dev.tr7zw.firstperson.FirstPersonModelCore;

public final class FirstPersonAPI {

    private static List<PlayerOffsetHandler> playerOffsetHandlers = new ArrayList<>();

    private FirstPersonAPI() {
        // private
    }
    
    /**
     * The current status of the FirstPerson Mod. If true, the mod will attempt to
     * apply the required modifications during rendering.
     * 
     * @return true if the mod is intended to be running right now
     */
    public static boolean isEnabled() {
        return FirstPersonModelCore.enabled;
    }

    /**
     * Change the enabled status. Simulates pressing the (by default) F6 hotkey.
     * 
     * @param enabled
     */
    public static void setEnabled(boolean enabled) {
        FirstPersonModelCore.enabled = enabled;
    }

    /**
     * Returns true when called during the first person player rendering. While this
     * is true, no heads/helmets/hats should be rendered!
     * 
     * @return
     */
    public static boolean isRenderingPlayer() {
        return FirstPersonModelCore.isRenderingPlayer;
    }

    /**
     * Adds a new {@link PlayerOffsetHandler}.
     * 
     * @param handler
     */
    public static void registerPlayerOffsetHandler(PlayerOffsetHandler handler) {
        playerOffsetHandlers.add(handler);
    }

    /**
     * Get all registered {@link PlayerOffsetHandler}. The list is mutable, this
     * might be used by mods to resolve mod conflicts where one mod includes a fix
     * for another mod.
     * 
     * @return
     */
    public static List<PlayerOffsetHandler> getPlayerOffsetHandlers() {
        return playerOffsetHandlers;
    }

}
