package dev.tr7zw.firstperson;

import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.firstperson.api.ActivationHandler;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.versionless.Constants;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

@RequiredArgsConstructor
public class LogicHandler {

    private final Minecraft client;
    private final FirstPersonModelCore fpm;
    @Getter
    private Vec3 offset = Vec3.ZERO; // Current offset used for rendering
    private Set<Item> autoVanillaHandItems = new HashSet<>();

    void registerDefaultHandlers() {
        FirstPersonAPI.registerPlayerHandler(new ActivationHandler() {

            @Override
            public boolean preventFirstperson() {
                if (client.player.isAutoSpinAttack())
                    return true;
                if (client.player.isFallFlying())
                    return true;
                if (client.player.getSwimAmount(1f) != 0 && !isCrawlingOrSwimming(client.player))
                    return true;
                // spotless:off
              //#if MC >= 11700
                if (client.player.isScoping())
                    return true;
              //#endif
              //spotless:on
                return false;
            }
        });
    }

    public boolean shouldApplyThirdPerson(boolean thirdPerson) {
        if (!fpm.isEnabled() || thirdPerson)
            return false;
        for (ActivationHandler handler : FirstPersonAPI.getActivationHandlers()) {
            if (handler.preventFirstperson()) {
                return false;
            }
        }
        return true;
    }

    public void updatePositionOffset(Entity player, Vec3 defValue) {
        // handle sleeping
        if (player == client.getCameraEntity() && client.player.isSleeping()) {
            offset = defValue;
            return;
        }
        double x, y, z = x = y = z = 0;
        AbstractClientPlayer abstractClientPlayerEntity_1;
        double realYaw;
        if (player == client.player && client.options.getCameraType() == CameraType.FIRST_PERSON
                && fpm.isRenderingPlayer()) {
            abstractClientPlayerEntity_1 = (AbstractClientPlayer) player;
            realYaw = Mth.rotLerp(client.getFrameTime(), abstractClientPlayerEntity_1.yBodyRotO,
                    abstractClientPlayerEntity_1.yBodyRot);
        } else {
            offset = defValue;
            return;
        }
        if (!abstractClientPlayerEntity_1.isLocalPlayer() || client.getCameraEntity() == abstractClientPlayerEntity_1) {
            float bodyOffset;
            if (isCrawlingOrSwimming(client.player)) {
                abstractClientPlayerEntity_1.yBodyRot = abstractClientPlayerEntity_1.yHeadRot;
                if (abstractClientPlayerEntity_1.xRotO > 0) {
                    bodyOffset = Constants.SWIM_UP_BODY_OFFSET;
                } else {
                    bodyOffset = Constants.SWIM_DOWN_BODY_OFFSET;
                }
            } else if (abstractClientPlayerEntity_1.isCrouching()) {
                bodyOffset = Constants.SNEAK_BODY_OFFSET + (fpm.getConfig().sneakXOffset / 100f);
            } else if (abstractClientPlayerEntity_1.isPassenger()) {
                if (abstractClientPlayerEntity_1.getVehicle() instanceof Boat
                        || abstractClientPlayerEntity_1.getVehicle() instanceof Minecart) {
                    realYaw = Mth.rotLerp(client.getFrameTime(), abstractClientPlayerEntity_1.yBodyRotO,
                            abstractClientPlayerEntity_1.yBodyRot);
                } else if (abstractClientPlayerEntity_1.getVehicle() instanceof LivingEntity) {
                    realYaw = Mth.rotLerp(client.getFrameTime(),
                            ((LivingEntity) abstractClientPlayerEntity_1.getVehicle()).yBodyRotO,
                            ((LivingEntity) abstractClientPlayerEntity_1.getVehicle()).yBodyRot);
                } else {
                    realYaw = Mth.rotLerp(client.getFrameTime(), abstractClientPlayerEntity_1.getVehicle().yRotO,
                            NMSHelper.getYRot(abstractClientPlayerEntity_1.getVehicle()));
                }
                bodyOffset = Constants.IN_VEHICLE_BODY_OFFSET + (fpm.getConfig().sitXOffset / 100f);
            } else {
                bodyOffset = 0.25f + (fpm.getConfig().xOffset / 100f);
            }
            x += bodyOffset * Math.sin(Math.toRadians(realYaw));
            z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
            if (isCrawlingOrSwimming(client.player)) {
                if (abstractClientPlayerEntity_1.xRotO > 0 && abstractClientPlayerEntity_1.isUnderWater()) {
                    y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.xRotO));
                } else {
                    y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.xRotO));
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

    public boolean showVanillaHands() {
        return fpm.getConfig().vanillaHands
                || autoVanillaHandItems.contains(client.player.getMainHandItem().getItem())
                || autoVanillaHandItems.contains(client.player.getOffhandItem().getItem());
    }

    public void addAutoVanillaHandsItem(Item item) {
        this.autoVanillaHandItems.add(item);
    }

    public void clearAutoVanillaHandsList() {
        this.autoVanillaHandItems.clear();
    }

    public void reloadAutoVanillaHandsSettings() {
        clearAutoVanillaHandsList();
        Item invalid = NMSHelper.getItem(new ResourceLocation("minecraft", "air"));
        for (String itemId : fpm.getConfig().autoVanillaHands) {
            try {
                Item item = NMSHelper.getItem(new ResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
                if (invalid != item)
                    addAutoVanillaHandsItem(item);
            } catch (Exception ex) {
                FirstPersonModelCore.LOGGER.info("Unknown item to add to the auto vanilla hold list: " + itemId);
            }
        }
        FirstPersonModelCore.LOGGER.info("Loaded Vanilla Hands items: {}", autoVanillaHandItems);
    }

}
