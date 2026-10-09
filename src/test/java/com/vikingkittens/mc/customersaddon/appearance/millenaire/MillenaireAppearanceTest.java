package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class MillenaireAppearanceTest {
    private static final ResourceLocation NORMAN = ResourceLocation.fromNamespaceAndPath("millenaire", "norman");
    private static final ResourceLocation FARMER = ResourceLocation.fromNamespaceAndPath("millenaire", "farmer");
    private static final MillenaireAppearanceSelection.Selection SELECTION =
            new MillenaireAppearanceSelection.Selection(NORMAN, FARMER);

    @Test
    void appearanceIsApplicableWhenResolverFindsAVillage() {
        MillenaireAppearance appearance = new MillenaireAppearance(villager -> Optional.of(SELECTION));

        assertTrue(appearance.isApplicable(mock(CustomersVillager.class)));
    }

    @Test
    void appearanceIsNotApplicableWithoutAVillage() {
        MillenaireAppearance appearance = new MillenaireAppearance(villager -> Optional.empty());

        assertFalse(appearance.isApplicable(mock(CustomersVillager.class)));
    }

    @Test
    void additionalPropertiesContainResolvedCulture() {
        MillenaireAppearance appearance = new MillenaireAppearance(villager -> Optional.of(SELECTION));

        Map<String, String> properties = appearance.getAdditionalProperties(mock(CustomersVillager.class));

        assertEquals(
                Map.of(
                        "millenaire.culture",
                        "millenaire:norman",
                        "millenaire.villager_type",
                        "millenaire:farmer"
                ),
                properties
        );
    }
}
