package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerRenderProxy;
import com.vikingkittens.mc.customersaddon.appearance.millenaire.MillenaireAppearance;

import org.millenaire.culture.ModCultures;
import org.millenaire.culture.VillagerType;
import org.millenaire.entity.MillVillager;
import org.millenaire.entity.ModEntities;

final class MillenaireVillagerProxy extends MillVillager implements CustomersVillagerRenderProxy {
    private WeakReference<CustomersVillager> source = new WeakReference<>(null);
    private WeakReference<Mob> sourceEntity = new WeakReference<>(null);
    private int sourceTick = Integer.MIN_VALUE;

    MillenaireVillagerProxy(Level level, Map<String, String> properties, float variationSeed) {
        super(ModEntities.MILL_VILLAGER.get(), level);
        applyProperties(properties, variationSeed);
    }

    void syncFrom(Mob entity, CustomersVillager villager) {
        source = new WeakReference<>(villager);
        sourceEntity = new WeakReference<>(entity);
        setPos(entity.getX(), entity.getY(), entity.getZ());
        setYRot(entity.getYRot());
        setXRot(entity.getXRot());
        yRotO = entity.yRotO;
        xRotO = entity.xRotO;
        yBodyRot = entity.yBodyRot;
        yBodyRotO = entity.yBodyRotO;
        yHeadRot = entity.yHeadRot;
        yHeadRotO = entity.yHeadRotO;
        tickCount = entity.tickCount;
        hurtTime = entity.hurtTime;
        deathTime = entity.deathTime;
        swinging = entity.swinging;
        swingingArm = entity.swingingArm;
        swingTime = entity.swingTime;
        oAttackAnim = entity.oAttackAnim;
        attackAnim = entity.attackAnim;
        if (sourceTick != entity.tickCount) {
            sourceTick = entity.tickCount;
            walkAnimation.update(entity.walkAnimation.speed(), 1.0F);
        }
        setPose(entity.getPose());
        setDeltaMovement(entity.getDeltaMovement());
        setCustomName(entity.getCustomName());
        setCustomNameVisible(entity.isCustomNameVisible());
        setFirstName(entity.getCustomName() == null ? "" : entity.getCustomName().getString());
        setFamilyName("");
        setInvisible(entity.isInvisible());
        setGlowingTag(entity.isCurrentlyGlowing());
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            setItemSlot(slot, entity.getItemBySlot(slot).copy());
        }
    }

    @Override
    public boolean isPassenger() {
        Mob entity = sourceEntity.get();
        return entity != null && entity.isPassenger();
    }

    @Override
    public @Nullable Entity getVehicle() {
        Mob entity = sourceEntity.get();
        return entity == null ? null : entity.getVehicle();
    }

    @Override
    public @Nullable CustomersVillager getCustomersVillagerSource() {
        return source.get();
    }

    @Override
    public boolean shouldRenderNameTag() {
        Mob entity = sourceEntity.get();
        return entity != null && entity.isCustomNameVisible();
    }

    private void applyProperties(Map<String, String> properties, float variationSeed) {
        ResourceLocation typeId = ResourceLocation.tryParse(properties.get(MillenaireAppearance.VILLAGER_TYPE));
        VillagerType type = typeId == null ? null : ModCultures.getVillagerType(typeId);
        if (type == null) {
            return;
        }
        setVillagerTypeId(typeId);
        MillenaireVillagerVisuals visuals = MillenaireVillagerVisuals.select(type, variationSeed);
        initAppearance(
                visuals.model(),
                visuals.texture(),
                visuals.cloth0(),
                visuals.cloth1(),
                visuals.scale(),
                "",
                "",
                ""
        );
        if (visuals.childSize() >= 0) {
            setChildSize(visuals.childSize());
        }
    }
}
