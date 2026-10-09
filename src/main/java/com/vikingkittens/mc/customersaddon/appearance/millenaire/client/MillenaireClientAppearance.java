package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerClientAppearance;
import com.vikingkittens.mc.customersaddon.appearance.millenaire.MillenaireAppearance;

import org.millenaire.culture.ModCultures;
import org.millenaire.culture.VillagerType;

final class MillenaireClientAppearance implements CustomersVillagerClientAppearance {
    private final MillenaireRenderer<?> renderer;

    MillenaireClientAppearance(EntityRendererProvider.Context context) {
        renderer = new MillenaireRenderer<>(context);
    }

    @Override
    public MobRenderer<?, ?> getRenderer(CustomersVillager villager) {
        return renderer;
    }

    @Override
    public float getNameTagOffset(CustomersVillager villager) {
        VillagerType type = visualType(villager);
        return villager instanceof Mob mob
                ? MillenaireSittingOffset.forOverheadItems(
                        visualScale(type, villager),
                        mob.getBbHeight(),
                        type != null && type.isChild()
                )
                : 0.0F;
    }

    @Override
    public Vec3 getSittingOffset(CustomersVillager villager) {
        VillagerType type = visualType(villager);
        return MillenaireSittingOffset.forScale(
                visualScale(type, villager),
                type != null && type.isChild()
        );
    }

    private static VillagerType visualType(CustomersVillager villager) {
        String value = villager.getAdditionalProperties().get(MillenaireAppearance.VILLAGER_TYPE);
        ResourceLocation typeId = value == null ? null : ResourceLocation.tryParse(value);
        return typeId == null ? null : ModCultures.getVillagerType(typeId);
    }

    private static float visualScale(VillagerType type, CustomersVillager villager) {
        return type == null ? 1.0F : MillenaireVillagerVisuals.scale(type, villager.getVariationSeed());
    }
}
