package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

import org.millenaire.culture.VillagerType;

final class MillenaireVillagerDefinitionSelector {
    private static final Set<String> MERCHANT_TAGS = Set.of("localmerchant", "foreignmerchant", "seller");

    private MillenaireVillagerDefinitionSelector() {}

    static Optional<ResourceLocation> select(
            ResourceLocation culture,
            CustomersVillagerType villagerType,
            float variationSeed,
            SelectionOptions options,
            Collection<Definition> definitions
    ) {
        List<Definition> ordinary = definitions.stream()
                .filter(definition -> definition.culture().equals(culture))
                .filter(definition -> isEligible(definition, options, false))
                .filter(definition -> !isMerchant(definition))
                .sorted(Comparator.comparing(definition -> definition.id().toString()))
                .toList();
        if (villagerType == CustomersVillagerType.SUPPLIER) {
            List<Definition> merchants = definitions.stream()
                    .filter(definition -> definition.culture().equals(culture))
                    .filter(definition -> isEligible(definition, options, true))
                    .filter(MillenaireVillagerDefinitionSelector::isMerchant)
                    .sorted(Comparator.comparing(definition -> definition.id().toString()))
                    .toList();
            if (!merchants.isEmpty()) {
                return Optional.of(select(merchants, variationSeed));
            }
        }
        return ordinary.isEmpty() ? Optional.empty() : Optional.of(select(ordinary, variationSeed));
    }

    static Definition from(VillagerType definition) {
        return new Definition(
                definition.id(),
                definition.culture(),
                definition.isChild(),
                definition.isHostile(),
                definition.isRaider(),
                definition.isDefensive(),
                Set.copyOf(definition.tags())
        );
    }

    private static boolean isEligible(Definition definition, SelectionOptions options, boolean merchantSelection) {
        if (!options.children() && definition.child()) {
            return false;
        }
        if (!options.hostileAndCombat()
                && (definition.hostile() || definition.raider() || definition.defensive())) {
            return false;
        }
        if (!options.leaders() && definition.tags().contains("chief")) {
            return false;
        }
        return options.visitors() || merchantSelection || !definition.tags().contains("visitor");
    }

    private static boolean isMerchant(Definition definition) {
        return definition.tags().stream().anyMatch(MERCHANT_TAGS::contains);
    }

    private static ResourceLocation select(List<Definition> definitions, float variationSeed) {
        int mixed = Float.floatToIntBits(variationSeed) ^ 0x3C6EF372;
        mixed ^= mixed >>> 16;
        mixed *= 0x7FEB352D;
        mixed ^= mixed >>> 15;
        return definitions.get(Math.floorMod(mixed, definitions.size())).id();
    }

    record Definition(
            ResourceLocation id,
            ResourceLocation culture,
            boolean child,
            boolean hostile,
            boolean raider,
            boolean defensive,
            Set<String> tags
    ) {}

    record SelectionOptions(boolean visitors, boolean leaders, boolean hostileAndCombat, boolean children) {
        static final SelectionOptions DEFAULT = new SelectionOptions(false, false, false, false);
    }
}
