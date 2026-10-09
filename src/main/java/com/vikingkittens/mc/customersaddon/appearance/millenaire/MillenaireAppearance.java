package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearance;

public final class MillenaireAppearance implements CustomersVillagerAppearance {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(
            CustomersMillenaireAppearanceMod.MOD_ID,
            "local_culture"
    );
    public static final String CULTURE = "millenaire.culture";
    public static final String VILLAGER_TYPE = "millenaire.villager_type";

    private final Function<CustomersVillager, Optional<MillenaireAppearanceSelection.Selection>> selectionResolver;

    public MillenaireAppearance() {
        this(MillenaireAppearanceSelection::resolve);
    }

    MillenaireAppearance(
            Function<CustomersVillager, Optional<MillenaireAppearanceSelection.Selection>> selectionResolver
    ) {
        this.selectionResolver = selectionResolver;
    }

    @Override
    public Component getName() {
        return Component.translatable("appearance.customers_millenaire_appearance.local_culture");
    }

    @Override
    public boolean isApplicable(CustomersVillager villager) {
        return selectionResolver.apply(villager).isPresent();
    }

    @Override
    public Map<String, String> getAdditionalProperties(CustomersVillager villager) {
        return selectionResolver.apply(villager)
                .map(selection -> Map.of(
                        CULTURE,
                        selection.culture().toString(),
                        VILLAGER_TYPE,
                        selection.villagerType().toString()
                ))
                .orElseGet(Map::of);
    }
}
