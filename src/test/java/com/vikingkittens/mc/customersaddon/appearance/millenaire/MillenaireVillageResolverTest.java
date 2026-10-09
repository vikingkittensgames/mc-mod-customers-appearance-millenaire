package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MillenaireVillageResolverTest {
    private static final BlockPos POSITION = new BlockPos(0, 64, 0);
    private static final ResourceLocation NORMAN = ResourceLocation.fromNamespaceAndPath("millenaire", "norman");
    private static final ResourceLocation JAPANESE = ResourceLocation.fromNamespaceAndPath("millenaire", "japanese");

    @Test
    void containingVillageIsPreferredWithoutRequestingClosest() {
        TestLookup lookup = new TestLookup(
                Optional.of(new MillenaireVillageResolver.VillageCandidate(new BlockPos(500, 64, 0), NORMAN)),
                Optional.of(new MillenaireVillageResolver.VillageCandidate(new BlockPos(1, 64, 0), JAPANESE))
        );

        assertEquals(Optional.of(NORMAN), MillenaireVillageResolver.resolve(POSITION, 128, lookup));
        assertFalse(lookup.closestRequested);
    }

    @Test
    void closestVillageInsideRadiusIsAccepted() {
        TestLookup lookup = new TestLookup(
                Optional.empty(),
                Optional.of(new MillenaireVillageResolver.VillageCandidate(new BlockPos(64, 64, 0), JAPANESE))
        );

        assertEquals(Optional.of(JAPANESE), MillenaireVillageResolver.resolve(POSITION, 128, lookup));
        assertTrue(lookup.closestRequested);
    }

    @Test
    void closestVillageOutsideRadiusIsRejected() {
        TestLookup lookup = new TestLookup(
                Optional.empty(),
                Optional.of(new MillenaireVillageResolver.VillageCandidate(new BlockPos(129, 64, 0), JAPANESE))
        );

        assertEquals(Optional.empty(), MillenaireVillageResolver.resolve(POSITION, 128, lookup));
    }

    @Test
    void noVillageReturnsEmpty() {
        assertEquals(
                Optional.empty(),
                MillenaireVillageResolver.resolve(POSITION, 128, new TestLookup(Optional.empty(), Optional.empty()))
        );
    }

    private static final class TestLookup implements MillenaireVillageResolver.VillageLookup {
        private final Optional<MillenaireVillageResolver.VillageCandidate> containing;
        private final Optional<MillenaireVillageResolver.VillageCandidate> closest;
        private boolean closestRequested;

        private TestLookup(
                Optional<MillenaireVillageResolver.VillageCandidate> containing,
                Optional<MillenaireVillageResolver.VillageCandidate> closest
        ) {
            this.containing = containing;
            this.closest = closest;
        }

        @Override
        public Optional<MillenaireVillageResolver.VillageCandidate> containing() {
            return containing;
        }

        @Override
        public Optional<MillenaireVillageResolver.VillageCandidate> closest() {
            closestRequested = true;
            return closest;
        }
    }
}
