package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearanceRegistries;

@Mod(CustomersMillenaireAppearanceMod.MOD_ID)
public final class CustomersMillenaireAppearanceMod {
    public static final String MOD_ID = "customers_millenaire_appearance";

    public CustomersMillenaireAppearanceMod(ModContainer modContainer) {
        CustomersVillagerAppearanceRegistries.appearances().register(
                MillenaireAppearance.ID,
                MillenaireAppearance::new
        );
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
