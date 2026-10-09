package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import org.junit.jupiter.api.Test;

import net.minecraft.world.phys.Vec3;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MillenaireSittingOffsetTest {
    @Test
    void fullSizeVillagersDoNotMove() {
        assertEquals(Vec3.ZERO, MillenaireSittingOffset.forScale(1.0F, false));
    }

    @Test
    void scaledVillagersAreRaisedToKeepTheirHipsAtSeatHeight() {
        Vec3 offset = MillenaireSittingOffset.forScale(0.8F, false);

        assertEquals(0.0D, offset.x, 0.0001D);
        assertEquals(0.15D, offset.y, 0.0001D);
        assertEquals(0.0D, offset.z, 0.0001D);
    }

    @Test
    void childSeatCorrectionIsScaledWithTheChildModel() {
        Vec3 offset = MillenaireSittingOffset.forScale(0.8F, true);

        assertEquals(0.0D, offset.x, 0.0001D);
        assertEquals(0.425D, offset.y, 0.0001D);
        assertEquals(0.0D, offset.z, 0.0001D);
    }

    @Test
    void overheadItemsFollowTheScaledModelHeight() {
        assertEquals(-0.39F, MillenaireSittingOffset.forOverheadItems(0.8F, 1.95F, false), 0.0001F);
        assertEquals(0.0F, MillenaireSittingOffset.forOverheadItems(1.0F, 1.95F, false), 0.0001F);
    }

    @Test
    void childOverheadItemsUseTheChildHeadHeightCorrection() {
        assertEquals(-1.14F, MillenaireSittingOffset.forOverheadItems(0.8F, 1.95F, true), 0.0001F);
    }
}
