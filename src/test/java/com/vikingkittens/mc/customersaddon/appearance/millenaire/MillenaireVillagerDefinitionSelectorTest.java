package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MillenaireVillagerDefinitionSelectorTest {
    private static final ResourceLocation NORMAN = id("norman");
    private static final MillenaireVillagerDefinitionSelector.SelectionOptions DEFAULT =
            MillenaireVillagerDefinitionSelector.SelectionOptions.DEFAULT;

    @Test
    void unsafeAndSpecialTaggedDefinitionsAreExcludedByDefault() {
        List<MillenaireVillagerDefinitionSelector.Definition> definitions = List.of(
                definition("farmer", false, false, false, false, Set.of()),
                definition("child", true, false, false, false, Set.of()),
                definition("bandit", false, true, true, false, Set.of()),
                definition("guard", false, false, false, true, Set.of()),
                definition("chief", false, false, false, false, Set.of("chief")),
                definition("visitor", false, false, false, false, Set.of("visitor"))
        );

        assertEquals(
                Optional.of(id("farmer")),
                MillenaireVillagerDefinitionSelector.select(
                        NORMAN,
                        CustomersVillagerType.CUSTOMER_NORMAL,
                        0.5F,
                        DEFAULT,
                        definitions
                )
        );
    }

    @Test
    void supplierPrefersMerchantDefinitions() {
        List<MillenaireVillagerDefinitionSelector.Definition> definitions = List.of(
                definition("farmer", false, false, false, false, Set.of()),
                definition("merchant", false, false, false, false, Set.of("foreignmerchant", "visitor"))
        );

        assertEquals(
                Optional.of(id("merchant")),
                MillenaireVillagerDefinitionSelector.select(
                        NORMAN,
                        CustomersVillagerType.SUPPLIER,
                        0.25F,
                        DEFAULT,
                        definitions
                )
        );
    }

    @Test
    void customersUseOrdinaryVillagersInsteadOfMerchantDefinitions() {
        List<MillenaireVillagerDefinitionSelector.Definition> definitions = List.of(
                definition("farmer", false, false, false, false, Set.of()),
                definition("merchant", false, false, false, false, Set.of("localmerchant"))
        );

        assertEquals(
                Optional.of(id("farmer")),
                MillenaireVillagerDefinitionSelector.select(
                        NORMAN,
                        CustomersVillagerType.CUSTOMER_NORMAL,
                        0.75F,
                        DEFAULT,
                        definitions
                )
        );
    }

    @Test
    void supplierFallsBackToOrdinaryAdultDefinitions() {
        List<MillenaireVillagerDefinitionSelector.Definition> definitions = List.of(
                definition("farmer", false, false, false, false, Set.of()),
                definition("child", true, false, false, false, Set.of())
        );

        assertEquals(
                Optional.of(id("farmer")),
                MillenaireVillagerDefinitionSelector.select(
                        NORMAN,
                        CustomersVillagerType.SUPPLIER,
                        0.25F,
                        DEFAULT,
                        definitions
                )
        );
    }

    @Test
    void optionsCanIncludeEachNormallyExcludedGroup() {
        MillenaireVillagerDefinitionSelector.SelectionOptions options =
                new MillenaireVillagerDefinitionSelector.SelectionOptions(true, true, true, true);
        List<MillenaireVillagerDefinitionSelector.Definition> definitions = List.of(
                definition("child", true, false, false, false, Set.of()),
                definition("bandit", false, true, true, false, Set.of()),
                definition("guard", false, false, false, true, Set.of()),
                definition("chief", false, false, false, false, Set.of("chief")),
                definition("visitor", false, false, false, false, Set.of("visitor"))
        );

        for (float seed = 0.0F; seed < 1.0F; seed += 0.01F) {
            assertFalse(MillenaireVillagerDefinitionSelector.select(
                    NORMAN,
                    CustomersVillagerType.CUSTOMER_CASUAL,
                    seed,
                    options,
                    definitions
            ).isEmpty());
        }
    }

    @Test
    void definitionsFromOtherCulturesAreIgnored() {
        MillenaireVillagerDefinitionSelector.Definition japanese = new MillenaireVillagerDefinitionSelector.Definition(
                id("japanese_farmer"),
                id("japanese"),
                false,
                false,
                false,
                false,
                Set.of()
        );

        assertEquals(
                Optional.empty(),
                MillenaireVillagerDefinitionSelector.select(
                        NORMAN,
                        CustomersVillagerType.CUSTOMER_NORMAL,
                        0.5F,
                        DEFAULT,
                        List.of(japanese)
                )
        );
    }

    private static MillenaireVillagerDefinitionSelector.Definition definition(
            String path,
            boolean child,
            boolean hostile,
            boolean raider,
            boolean defensive,
            Set<String> tags
    ) {
        return new MillenaireVillagerDefinitionSelector.Definition(
                id(path),
                NORMAN,
                child,
                hostile,
                raider,
                defensive,
                tags
        );
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("millenaire", path);
    }
}
