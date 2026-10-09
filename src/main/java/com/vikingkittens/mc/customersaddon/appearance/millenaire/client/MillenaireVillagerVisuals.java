package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import org.millenaire.culture.VillagerType;
import org.millenaire.entity.MillVillager;
import org.millenaire.entity.ModelType;
import org.millenaire.entity.VillagerAppearanceFactory;

record MillenaireVillagerVisuals(
        ModelType model,
        @Nullable ResourceLocation texture,
        @Nullable ResourceLocation cloth0,
        @Nullable ResourceLocation cloth1,
        float scale,
        int childSize
) {
    static MillenaireVillagerVisuals select(VillagerType type, float variationSeed) {
        ResourceLocation texture = select(type.textures(), variationSeed, 0x3C6EF372);
        ResourceLocation cloth0 = selectCloth(type, 0, variationSeed, 0x1BF5A9D7);
        ResourceLocation cloth1 = selectCloth(type, 1, variationSeed, 0x5A17C9E3);
        int childSize = childSize(type, variationSeed);
        float scale = scale(type, variationSeed, childSize);
        return new MillenaireVillagerVisuals(type.modelType(), texture, cloth0, cloth1, scale, childSize);
    }

    static float scale(VillagerType type, float variationSeed) {
        return scale(type, variationSeed, childSize(type, variationSeed));
    }

    private static float scale(VillagerType type, float variationSeed, int childSize) {
        if (childSize >= 0) {
            return VillagerAppearanceFactory.computeChildScale(childSize, type.gender());
        }
        return type.baseScale() * (0.8F + unitValue(variationSeed, 0x72E4B1C5) * 0.09F);
    }

    private static int childSize(VillagerType type, float variationSeed) {
        return type.isChild()
                ? Math.floorMod(mix(variationSeed, 0x4F1BBCDC), MillVillager.MAX_CHILD_SIZE + 1)
                : -1;
    }

    private static ResourceLocation selectCloth(VillagerType type, int layer, float seed, int salt) {
        String clothName = type.hasNaturalLayer(layer) ? "natural" : "free";
        VillagerType.ClothSet clothSet = type.clothes().get(clothName);
        if (clothSet == null) {
            return null;
        }
        return select(layer == 0 ? clothSet.layer0() : clothSet.layer1(), seed, salt);
    }

    private static ResourceLocation select(List<ResourceLocation> values, float seed, int salt) {
        return values.isEmpty() ? null : values.get(Math.floorMod(mix(seed, salt), values.size()));
    }

    private static float unitValue(float seed, int salt) {
        return (mix(seed, salt) & 0x7FFFFFFF) / (float) Integer.MAX_VALUE;
    }

    private static int mix(float seed, int salt) {
        int mixed = Float.floatToIntBits(seed) ^ salt;
        mixed ^= mixed >>> 16;
        mixed *= 0x7FEB352D;
        return mixed ^ mixed >>> 15;
    }
}
