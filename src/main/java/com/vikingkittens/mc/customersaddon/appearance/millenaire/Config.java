package com.vikingkittens.mc.customersaddon.appearance.millenaire;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_NEARBY_VILLAGE_DISTANCE = BUILDER
            .translation("config.customers_millenaire_appearance.max_nearby_village_distance")
            .defineInRange("maxNearbyVillageDistance", 128, 0, 4096);
    public static final ModConfigSpec.BooleanValue INCLUDE_VISITORS = BUILDER
            .translation("config.customers_millenaire_appearance.include_visitors")
            .define("includeVisitors", false);
    public static final ModConfigSpec.BooleanValue INCLUDE_LEADERS = BUILDER
            .translation("config.customers_millenaire_appearance.include_leaders")
            .define("includeLeaders", false);
    public static final ModConfigSpec.BooleanValue INCLUDE_HOSTILE_AND_COMBAT = BUILDER
            .translation("config.customers_millenaire_appearance.include_hostile_and_combat")
            .define("includeHostileAndCombat", false);
    public static final ModConfigSpec.BooleanValue INCLUDE_CHILDREN = BUILDER
            .translation("config.customers_millenaire_appearance.include_children")
            .define("includeChildren", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {}

    static MillenaireVillagerDefinitionSelector.SelectionOptions selectionOptions() {
        return new MillenaireVillagerDefinitionSelector.SelectionOptions(
                INCLUDE_VISITORS.get(),
                INCLUDE_LEADERS.get(),
                INCLUDE_HOSTILE_AND_COMBAT.get(),
                INCLUDE_CHILDREN.get()
        );
    }
}
