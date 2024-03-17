package dev.tr7zw.firstperson;

import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.firstperson.access.PlayerAccess;
import dev.tr7zw.firstperson.api.ActivationHandler;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.versionless.Constants;
import dev.tr7zw.firstperson.versionless.FirstPersonBase;
import dev.tr7zw.firstperson.versionless.config.VanillaHands;
import dev.tr7zw.util.NMSHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

@RequiredArgsConstructor
public class LogicHandler {

    private final Minecraft client;
    private final FirstPersonModelCore fpm;
    @Getter
    private Vec3 offset = Vec3.ZERO; // Current offset used for rendering
    private Set<Item> autoVanillaHandItems = new HashSet<>();
    private Set<Item> autoDisableItems = new HashSet<>();

    private int spinTimer = 0;

    void registerDefaultHandlers() {
        FirstPersonAPI.registerPlayerHandler((ActivationHandler) () -> {
            if (client.player.isAutoSpinAttack()) {//TODO SPIN ATTACK MODEL OFFSET FIX
                spinTimer = spinTimer <= 0 ? 132 : spinTimer;
            }
            if ((spinTimer -= spinTimer < 0 ? 0 : 1) > 0) {
                return true;
            }
            if (autoDisableItems.contains(client.player.getMainHandItem().getItem())
                    || autoDisableItems.contains(client.player.getOffhandItem().getItem())) {
                return true;
            }
            // spotless:off
            //#if MC >= 11700
            if (client.player.isScoping()) {
                return true;
            }
            //#endif
            //spotless:on
            return false;
        });
    }

    /**
     * Checks rather the mod should render at all.
     * 
     * @param thirdPerson
     * @return
     */
    public boolean shouldApplyThirdPerson(boolean thirdPerson) {
        if (!fpm.isEnabled() || thirdPerson) {
            return false;
        }
        for (ActivationHandler handler : FirstPersonAPI.getActivationHandlers()) {
            if (handler.preventFirstperson()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the X/Z offset applied to the player model to get it relative to
     * the vanilla camera position
     * 
     * @param entity
     * @param defValue
     */
    public void updatePositionOffset(Entity entity, Vec3 defValue) {
        // handle sleeping
        if (entity == client.getCameraEntity() && client.player.isSleeping()) {
            offset = defValue;
            return;
        }
        double x = 0;
        double y = 0;
        double z = 0;
        AbstractClientPlayer player;
        double realYaw;
        if ((entity != client.player) || (client.options.getCameraType() != CameraType.FIRST_PERSON)
                || !fpm.isRenderingPlayer()) {
            offset = defValue;
            return;
        }
        player = (AbstractClientPlayer) entity;
        realYaw = Mth.rotLerp(client.getFrameTime(), player.yBodyRotO, player.yBodyRot);
        if (!player.isLocalPlayer() || client.getCameraEntity() == player) {
            float bodyOffset;
            if (isCrawlingOrSwimming(client.player)) {
                player.yBodyRot = player.yHeadRot;
                if (player.xRotO > 0) {
                    bodyOffset = Constants.SWIM_UP_BODY_OFFSET;
                } else {
                    bodyOffset = Constants.SWIM_DOWN_BODY_OFFSET;
                }
                // some mods seem to break the isCrouching method
            } else if (player.isCrouching() || player.getPose() == Pose.CROUCHING) {
                bodyOffset = Constants.SNEAK_BODY_OFFSET + fpm.getConfig().sneakXOffset / 100f;
            } else if (player.isPassenger()) {
                if (player.getVehicle() instanceof Boat || player.getVehicle() instanceof Minecart) {
                    realYaw = Mth.rotLerp(client.getFrameTime(), player.yBodyRotO, player.yBodyRot);
                } else if (player.getVehicle() instanceof LivingEntity living) {
                    realYaw = Mth.rotLerp(client.getFrameTime(), living.yBodyRotO, living.yBodyRot);
                } else {
                    realYaw = Mth.rotLerp(client.getFrameTime(), player.getVehicle().yRotO,
                            NMSHelper.getYRot(player.getVehicle()));
                }
                bodyOffset = Constants.IN_VEHICLE_BODY_OFFSET + fpm.getConfig().sitXOffset / 100f;
            } else {
                bodyOffset = 0.25f + fpm.getConfig().xOffset / 100f;
            }
            x += bodyOffset * Math.sin(Math.toRadians(realYaw));
            z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
            if (isCrawlingOrSwimming(client.player)) {
                if (player.xRotO > 0 && player.isUnderWater()) {
                    y += 0.6f * Math.sin(Math.toRadians(player.xRotO));
                } else {
                    y += 0.01f * -Math.sin(Math.toRadians(player.xRotO));
                }
            }

        }
        offset = new Vec3(x, y, z);
    }

    /**
     * Util method to quicker find where swimming is referenced
     * 
     * @param player
     * @return
     */
    public boolean isSwimming(Player player) {
        return player.isSwimming();
    }

    /**
     * Util method to quicker find where the crawling/swimming animation is
     * referenced
     * 
     * @param player
     * @return
     */
    public boolean isCrawlingOrSwimming(Player player) {
        return player.isVisuallySwimming();
    }

    public boolean isUseSpear(Player player, ItemStack itemStack) { //TODO SPEAR FIX
        return player.getUseItem() == itemStack && itemStack.getItem().getUseAnimation(itemStack).equals(UseAnim.SPEAR);
    }

    public boolean showVanillaHands() {
        return showVanillaHands(client.player);
    }

    public boolean showVanillaHands(LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerAccess playerAccess) {
            return showVanillaHands(playerAccess.getInventory().getSelected(),
                    playerAccess.getInventory().offhand.get(0));
        }
        return false;
    }

    /**
     * Don't skip the vanilla first person arm rendering
     * 
     * @param mainhand
     * @param offhand
     * @return
     */
    public boolean showVanillaHands(ItemStack mainhand, ItemStack offhand) {
        return fpm.getConfig().vanillaHandsMode == VanillaHands.ALL
                || fpm.getConfig().vanillaHandsMode == VanillaHands.ALL_DOUBLE
                || (fpm.getConfig().vanillaHandsMode == VanillaHands.ITEMS
                        && (!mainhand.isEmpty() || !offhand.isEmpty()))
                || autoVanillaHandItems.contains(mainhand.getItem()) || autoVanillaHandItems.contains(offhand.getItem())
                || autoDisableItems.contains(mainhand.getItem()) || autoDisableItems.contains(offhand.getItem());
    }

    public boolean hideArmsAndItems() {
        return hideArmsAndItems(client.player);
    }

    public boolean hideArmsAndItems(LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerAccess playerAccess) {
            return hideArmsAndItems(playerAccess.getInventory().getSelected(),
                    playerAccess.getInventory().offhand.get(0));
        }
        return false;
    }

    /**
     * Should the models arms and items not be rendered?
     * 
     * @param mainhand
     * @param offhand
     * @return
     */
    public boolean hideArmsAndItems(ItemStack mainhand, ItemStack offhand) {
        if(lookingDown()) {
            return false;
        }
        return fpm.getConfig().vanillaHandsMode != VanillaHands.OFF || autoVanillaHandItems.contains(mainhand.getItem())
                || autoVanillaHandItems.contains(offhand.getItem()) || autoDisableItems.contains(mainhand.getItem())
                || autoDisableItems.contains(offhand.getItem());
    }

    public boolean dynamicHandsEnabled() {
        return dynamicHandsEnabled(client.player);
    }
    
    public boolean dynamicHandsEnabled(LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerAccess playerAccess) {
            return dynamicHandsEnabled(playerAccess.getInventory().getSelected(),
                    playerAccess.getInventory().offhand.get(0));
        }
        return false;
    }
    
    /**
     * True is dynamic hands is enabled and could apply at this moment
     * 
     * @param mainhand
     * @param offhand
     * @return
     */
    public boolean dynamicHandsEnabled(ItemStack mainhand, ItemStack offhand) {
        return fpm.getConfig().dynamicMode && !(autoVanillaHandItems.contains(mainhand.getItem())
                || autoVanillaHandItems.contains(offhand.getItem()) || autoDisableItems.contains(mainhand.getItem())
                || autoDisableItems.contains(offhand.getItem()));
    }

    /**
     * Is Dynamic hands enabled and the player looking down?
     * 
     * @return
     */
    public boolean lookingDown() {
        return client.player.getXRot() > 30;
    }//TODO FIX DOUBLE RECHECK dynamicHandsEnabled()

    public void addAutoVanillaHandsItem(Item item) {
        autoVanillaHandItems.add(item);
    }

    public void addAutoDisableItem(Item item) {
        autoDisableItems.add(item);
    }

    public void reloadAutoVanillaHandsSettings() {
        autoVanillaHandItems.clear();
        autoDisableItems.clear();
        Item invalid = NMSHelper.getItem(new ResourceLocation("minecraft", "air"));
        for (String itemId : fpm.getConfig().autoVanillaHands) {
            try {
                Item item = NMSHelper.getItem(new ResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
                if (invalid != item) {
                    addAutoVanillaHandsItem(item);
                }
            } catch (Exception ex) {
                FirstPersonBase.LOGGER.info("Unknown item to add to the auto vanilla hold list: {}", itemId);
            }
        }
        FirstPersonBase.LOGGER.info("Loaded Vanilla Hands items: {}", autoVanillaHandItems);
        for (String itemId : fpm.getConfig().autoToggleModItems) {
            try {
                Item item = NMSHelper.getItem(new ResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
                if (invalid != item) {
                    addAutoDisableItem(item);
                }
            } catch (Exception ex) {
                FirstPersonBase.LOGGER.info("Unknown item to add to the auto disable list: {}", itemId);
            }
        }
        FirstPersonBase.LOGGER.info("Loaded Auto Disable items: {}", autoDisableItems);
    }

}
