package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerClientAppearances;
import com.vikingkittens.mc.customersaddon.appearance.millenaire.CustomersMillenaireAppearanceMod;
import com.vikingkittens.mc.customersaddon.appearance.millenaire.MillenaireAppearance;

@EventBusSubscriber(modid = CustomersMillenaireAppearanceMod.MOD_ID, value = Dist.CLIENT)
public final class MillenaireClientEvents {
    private MillenaireClientEvents() {}

    @SubscribeEvent
    public static void registerAppearance(EntityRenderersEvent.RegisterRenderers event) {
        CustomersVillagerClientAppearances.register(MillenaireAppearance.ID, MillenaireClientAppearance::new);
    }
}
