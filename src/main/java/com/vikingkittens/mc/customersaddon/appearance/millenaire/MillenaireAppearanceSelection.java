package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.Optional;

import net.minecraft.resources.ResourceLocation;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;

import org.millenaire.culture.ModCultures;

final class MillenaireAppearanceSelection {
    private MillenaireAppearanceSelection() {}

    static Optional<Selection> resolve(CustomersVillager villager) {
        return MillenaireVillageResolver.resolve(villager, Config.MAX_NEARBY_VILLAGE_DISTANCE.get())
                .flatMap(culture -> MillenaireVillagerDefinitionSelector.select(
                                culture,
                                villager.getCustomersVillagerType(),
                                villager.getVariationSeed(),
                                Config.selectionOptions(),
                                ModCultures.getAllVillagerTypes().values().stream()
                                        .map(MillenaireVillagerDefinitionSelector::from)
                                        .toList()
                        )
                        .map(villagerType -> new Selection(culture, villagerType)));
    }

    record Selection(ResourceLocation culture, ResourceLocation villagerType) {}
}
