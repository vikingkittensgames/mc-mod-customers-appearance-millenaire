package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import net.minecraft.world.phys.Vec3;

final class MillenaireSittingOffset {
    private static final double HUMANOID_SEATED_HIP_HEIGHT = 0.75D;
    private static final double CHILD_SEATED_OFFSET = 0.5D;
    private static final double CHILD_SEATED_HEIGHT_ADJUSTMENT = 0.125D;
    private static final double CHILD_OVERHEAD_OFFSET = 0.75D;

    private MillenaireSittingOffset() {}

    static Vec3 forScale(float scale, boolean child) {
        return new Vec3(
                0.0D,
                HUMANOID_SEATED_HIP_HEIGHT * (1.0D - scale) + childOffset(scale, child),
                0.0D
        );
    }

    static float forOverheadItems(float scale, float unscaledHeight, boolean child) {
        double childOverheadOffset = child ? CHILD_OVERHEAD_OFFSET : 0.0D;
        return (float) (unscaledHeight * (scale - 1.0F) - childOverheadOffset);
    }

    private static double childOffset(float scale, boolean child) {
        return child ? CHILD_SEATED_OFFSET * scale - CHILD_SEATED_HEIGHT_ADJUSTMENT : 0.0D;
    }
}
