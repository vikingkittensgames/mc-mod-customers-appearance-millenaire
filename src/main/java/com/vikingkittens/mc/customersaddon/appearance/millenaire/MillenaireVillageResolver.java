package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.Comparator;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;

import org.millenaire.village.Village;
import org.millenaire.village.VillageManager;
import org.millenaire.village.VillageSavedData;

final class MillenaireVillageResolver {
    static final int DEFAULT_MAX_DISTANCE = 128;

    private MillenaireVillageResolver() {}

    static Optional<ResourceLocation> resolve(CustomersVillager villager, int maxDistance) {
        BlockPos position = villager.getCustomersSpawnerPosition();
        if (!(villager.getCustomersLevel() instanceof ServerLevel level) || position == null) {
            return Optional.empty();
        }

        VillageManager manager = VillageSavedData.get(level).getVillageManager();
        return resolve(position, maxDistance, new VillageLookup() {
            @Override
            public Optional<VillageCandidate> containing() {
                Vec3 center = Vec3.atCenterOf(position);
                return manager.getAllVillages().stream()
                        .filter(village -> village.computeBounds().contains(center))
                        .min(Comparator.comparingDouble(village -> position.distSqr(village.getCenter())))
                        .map(MillenaireVillageResolver::candidate);
            }

            @Override
            public Optional<VillageCandidate> closest() {
                return Optional.ofNullable(manager.findNearestVillage(position, maxDistance))
                        .map(MillenaireVillageResolver::candidate);
            }
        });
    }

    static Optional<ResourceLocation> resolve(BlockPos position, int maxDistance, VillageLookup lookup) {
        Optional<VillageCandidate> containing = lookup.containing();
        if (containing.isPresent()) {
            return Optional.of(containing.orElseThrow().culture());
        }

        long distance = maxDistance;
        double maximumDistanceSquared = distance * distance;
        return lookup.closest()
                .filter(candidate -> position.distSqr(candidate.center()) <= maximumDistanceSquared)
                .map(VillageCandidate::culture);
    }

    private static VillageCandidate candidate(Village village) {
        return new VillageCandidate(village.getCenter(), village.getCultureId());
    }

    interface VillageLookup {
        Optional<VillageCandidate> containing();

        Optional<VillageCandidate> closest();
    }

    record VillageCandidate(BlockPos center, ResourceLocation culture) {}
}
