package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

import org.millenaire.culture.Gender;
import org.millenaire.culture.VillagerType;
import org.millenaire.entity.ModelType;
import org.millenaire.entity.VillagerAppearanceFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MillenaireVillagerVisualsTest {
    @Test
    void selectionIsDeterministicAndUsesLoadedDefinitionAssets() {
        VillagerType type = definition(false);

        MillenaireVillagerVisuals first = MillenaireVillagerVisuals.select(type, 0.625F);

        assertEquals(first, MillenaireVillagerVisuals.select(type, 0.625F));
        assertTrue(type.textures().contains(first.texture()));
        assertTrue(type.clothes().get("natural").layer0().contains(first.cloth0()));
        assertTrue(type.clothes().get("free").layer1().contains(first.cloth1()));
        assertTrue(first.scale() >= 0.8F && first.scale() <= 0.89F);
    }

    @Test
    void childSizeIsSelectedOnlyForChildDefinitions() {
        assertEquals(-1, MillenaireVillagerVisuals.select(definition(false), 0.25F).childSize());
        MillenaireVillagerVisuals child = MillenaireVillagerVisuals.select(definition(true), 0.25F);
        assertTrue(child.childSize() >= 0 && child.childSize() <= 20);
        assertEquals(
                VillagerAppearanceFactory.computeChildScale(child.childSize(), Gender.MALE),
                child.scale()
        );
    }

    private static VillagerType definition(boolean child) {
        VillagerType type = mock(VillagerType.class);
        when(type.modelType()).thenReturn(ModelType.MALE);
        when(type.textures()).thenReturn(List.of(id("texture_1"), id("texture_2")));
        when(type.baseScale()).thenReturn(1.0F);
        when(type.isChild()).thenReturn(child);
        when(type.gender()).thenReturn(Gender.MALE);
        when(type.hasNaturalLayer(0)).thenReturn(true);
        when(type.hasNaturalLayer(1)).thenReturn(false);
        when(type.clothes()).thenReturn(Map.of(
                "natural",
                new VillagerType.ClothSet(List.of(id("natural_1"), id("natural_2")), List.of()),
                "free",
                new VillagerType.ClothSet(List.of(), List.of(id("free_1"), id("free_2")))
        ));
        return type;
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("millenaire", path);
    }
}
