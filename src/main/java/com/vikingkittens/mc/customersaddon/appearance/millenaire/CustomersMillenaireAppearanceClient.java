package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CustomersMillenaireAppearanceMod.MOD_ID, dist = Dist.CLIENT)
public final class CustomersMillenaireAppearanceClient {
    public CustomersMillenaireAppearanceClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
