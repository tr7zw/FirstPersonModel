package dev.tr7zw.firstperson.access;

public interface LivingEntityRenderStateAccess {

    void setIsCameraEntity(boolean value);

    boolean isCameraEntity();

    default void setHideArms(boolean value) {
        setHideLeftArm(value);
        setHideRightArm(value);
    }

    void setHideRightArm(boolean value);

    void setHideLeftArm(boolean value);

    boolean hideLeftArm();

    boolean hideRightArm();

    void setHideBody(boolean value);

    boolean hideBody();

    void setArmOffset(float offset);

    float getArmOffset();

}
