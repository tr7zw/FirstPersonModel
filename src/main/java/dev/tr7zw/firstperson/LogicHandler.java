package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.api.*;
import dev.tr7zw.firstperson.versionless.*;
import dev.tr7zw.firstperson.versionless.config.*;
import dev.tr7zw.transition.mc.*;
import lombok.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.*;

import java.util.*;

@RequiredArgsConstructor
public class LogicHandler {

    private final Minecraft client;
    private final FirstPersonModelCore fpm;
    @Getter
    private Vec3 offset = Vec3.ZERO; // Current offset used for rendering
    private Set<Item> autoVanillaHandItems = new HashSet<>();
    private Set<Item> autoDisableItems = new HashSet<>();
    private long timeout = 0;

    void registerDefaultHandlers() {
        FirstPersonAPI.registerPlayerHandler((ActivationHandler) () -> {
            if (client.player.isSleeping() || client.player.isAutoSpinAttack() || client.player.isFallFlying()
                    || (client.player.getSwimAmount(1f) != 0 && !isCrawlingOrSwimming(client.player))) {
                timeout = System.currentTimeMillis() + 100;
                return true;
            }
            // FIXME: Evil hack to fix weird 1 frame-ish issues when landing with an elytra
            if (System.currentTimeMillis() < timeout) {
                return true;
            }
            if (autoDisableItems.contains(client.player.getMainHandItem().getItem())
                    || autoDisableItems.contains(client.player.getOffhandItem().getItem())) {
                return true;
            }
            //? if >= 1.17.0 {

            if (client.player.isScoping()) {
                return true;
            }
            //? }
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
     * @param delta
     */
    public void updatePositionOffset(Entity entity, float delta) {
        offset = Vec3.ZERO;
        // handle sleeping
        if (entity == client.getCameraEntity() && client.player.isSleeping()) {
            return;
        }
        double x = 0;
        double y = 0;
        double z = 0;
        AbstractClientPlayer player;
        double realYaw;
        if ((entity != client.player) || (client.options.getCameraType() != CameraType.FIRST_PERSON)) {
            return;
        }
        player = (AbstractClientPlayer) entity;
        realYaw = Mth.rotLerp(delta, player.yBodyRotO, player.yBodyRot);
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
                    realYaw = Mth.rotLerp(delta, player.yBodyRotO, player.yBodyRot);
                } else if (player.getVehicle() instanceof LivingEntity living) {
                    realYaw = calculateBodyRot(Mth.rotLerp(delta, living.yBodyRotO, living.yBodyRot),
                            EntityUtil.getYRot(player));
                } else {
                    // Non living entities don't use any custom rotation
                    //                    realYaw = Mth.rotLerp(client.getFrameTime(), player.getVehicle().yRotO,
                    //                            NMSHelper.getYRot(player.getVehicle()));
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
        for (PlayerOffsetHandler handler : FirstPersonAPI.getPlayerOffsetHandlers()) {
            offset = handler.applyOffset(client.player, delta, Vec3.ZERO, offset);
        }
    }

    private static float calculateBodyRot(float entityBodyRot, float riderHeadRot) {
        // Wrap the head rotation to the range [-180, 180]
        float wrappedHeadRot = Mth.wrapDegrees(riderHeadRot);

        // Calculate the difference between the head and body rotation
        float rotDiff = Mth.wrapDegrees(wrappedHeadRot - entityBodyRot);

        // If the difference is more than 50 degrees, adjust the body rotation
        if (Mth.abs(rotDiff) > 50.0F) {
            // Pull the body along with the head
            entityBodyRot = wrappedHeadRot - 50.0F * Math.signum(rotDiff);
        }

        // Ensure the body rotation is wrapped to [-180, 180]
        entityBodyRot = Mth.wrapDegrees(entityBodyRot);

        return entityBodyRot;
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

    public boolean showVanillaHands() {
        return showVanillaHands(client.player);
    }

    public boolean showVanillaHands(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            return showVanillaHands(InventoryUtil.getSelected(InventoryUtil.getInventory(player)),
                    InventoryUtil.getOffhand(InventoryUtil.getInventory(player)));
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
        if (livingEntity instanceof Player player) {
            return hideArmsAndItems(livingEntity, InventoryUtil.getSelected(InventoryUtil.getInventory(player)),
                    InventoryUtil.getOffhand(InventoryUtil.getInventory(player)));
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
    public boolean hideArmsAndItems(LivingEntity livingEntity, ItemStack mainhand, ItemStack offhand) {
        if (FirstPersonModelCore.instance.getConfig().vanillaHandsSkipSwimming && livingEntity instanceof Player player
                && isSwimming(player)) {
            return false;
        }
        if (lookingDown()) {
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
        if (livingEntity instanceof Player player) {
            return dynamicHandsEnabled(livingEntity, InventoryUtil.getSelected(InventoryUtil.getInventory(player)),
                    InventoryUtil.getOffhand(InventoryUtil.getInventory(player)));
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
    public boolean dynamicHandsEnabled(LivingEntity livingEntity, ItemStack mainhand, ItemStack offhand) {
        if (FirstPersonModelCore.instance.getConfig().vanillaHandsSkipSwimming && livingEntity instanceof Player player
                && isSwimming(player)) {
            return false;
        }
        return fpm.getConfig().dynamicMode && fpm.getConfig().vanillaHandsMode != VanillaHands.OFF
                && !(autoVanillaHandItems.contains(mainhand.getItem())
                        || autoVanillaHandItems.contains(offhand.getItem())
                        || autoDisableItems.contains(mainhand.getItem())
                        || autoDisableItems.contains(offhand.getItem()));
    }

    /**
     * Is Dynamic hands enabled and the player looking down?
     * 
     * @return
     */
    public boolean lookingDown() {
        return lookingDown(client.player);
    }

    public boolean lookingDown(LivingEntity livingEntity) {
        return dynamicHandsEnabled() && EntityUtil.getXRot(livingEntity) > 30;
    }

    //? if >= 1.21.6 {
    public boolean lookingDown(LivingEntityRenderState state) {
        return dynamicHandsEnabled() && state.xRot > 30;
    }
    //? }

    public void addAutoVanillaHandsItem(Item item) {
        autoVanillaHandItems.add(item);
    }

    public void addAutoDisableItem(Item item) {
        autoDisableItems.add(item);
    }

    public void reloadAutoVanillaHandsSettings() {
        autoVanillaHandItems.clear();
        autoDisableItems.clear();
        Item invalid = ItemUtil.getItem(GeneralUtil.getResourceLocation("minecraft", "air"));
        for (String itemId : fpm.getConfig().autoVanillaHands) {
            try {
                Item item = ItemUtil
                        .getItem(GeneralUtil.getResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
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
                Item item = ItemUtil
                        .getItem(GeneralUtil.getResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
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
