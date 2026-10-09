package com.vikingkittens.mc.customersaddon.appearance.millenaire.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import org.millenaire.client.render.MillVillagerRenderer;
import org.millenaire.entity.MillVillager;

final class MillenaireDelegateRenderer extends MillVillagerRenderer {
    MillenaireDelegateRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected boolean shouldShowName(MillVillager villager) {
        return villager instanceof MillenaireVillagerProxy proxy && proxy.shouldRenderNameTag();
    }
}
