package com.robertx22.anti_mob_cheese.configs;

import com.robertx22.anti_mob_cheese.main.ModAction;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class CheeseConfig {

    public static final ForgeConfigSpec commonSpec;
    public static final CheeseConfig COMMON;

    static {
        final Pair<CheeseConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CheeseConfig::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    CheeseConfig(ForgeConfigSpec.Builder b) {
        b.comment("Settings")
                .push("general");

        ENABLE_ANTI_MOB_FARM = b
                .define("enable_anti_mob_farm", true);
        AFFECT_PEACEFUL_ANIMALS = b
                .define("affect_animals", false);
        AFFECT_VANILLA_LOOT_TABLES = b
                .define("affect_vanilla_loot", false);
        AFFECT_AGE_OF_EXILE_LOOT = b
                .define("affect_age_of_exile_loot", true);
        AFFECT_AGE_OF_EXILE_EXP = b
                .define("affect_age_of_exile_exp", true);

        MIN_PLAYER_DMG_TO_GET_LOOT = b
                .defineInRange("min_player_dmg_for_loot", 0.5D, 0D, 1D);

        ON_MOB_KILLED_DECREASE_BY = b
                .defineInRange("mob_kill_chunk_penalty", 0.02D, 0D, 1D);
        ON_MINUTE_PASSED_INCREASE_BY = b
                .defineInRange("penalty_regen_per_minute", 0.05D, 0D, 1D);
        FREE_MOB_KILLS_BEFORE_PENALTY_STARTS = b
                .defineInRange("one_time_free_kills_per_chunk", 15, 0, 1000);
        ADD_FREE_KILLS_ON_CHEST_LOOT = b
                .defineInRange("added_free_kills_on_chest_looted", 5, 0, 1000);

        List<String> dim = new ArrayList<>();
        dim.add("mmorpg:dungeon");

        DIMENSIONS_EXCLUDED = b
                .defineList("excluded_dimensions", dim, x -> true);
        ENTITIES_EXCLUDED = b
                .defineList("excluded_entities", new ArrayList<>(), x -> true);

        b.pop();
    }

    public static CheeseConfig get() {
        return COMMON;
    }

    public ForgeConfigSpec.ConfigValue<? extends List> DIMENSIONS_EXCLUDED;
    public ForgeConfigSpec.ConfigValue<? extends List> ENTITIES_EXCLUDED;

    public ForgeConfigSpec.BooleanValue ENABLE_ANTI_MOB_FARM;

    public ForgeConfigSpec.DoubleValue ON_MOB_KILLED_DECREASE_BY;
    public ForgeConfigSpec.DoubleValue ON_MINUTE_PASSED_INCREASE_BY;

    public ForgeConfigSpec.IntValue FREE_MOB_KILLS_BEFORE_PENALTY_STARTS;
    public ForgeConfigSpec.IntValue ADD_FREE_KILLS_ON_CHEST_LOOT;

    public ForgeConfigSpec.BooleanValue AFFECT_PEACEFUL_ANIMALS;

    private ForgeConfigSpec.BooleanValue AFFECT_VANILLA_LOOT_TABLES;
    private ForgeConfigSpec.BooleanValue AFFECT_AGE_OF_EXILE_LOOT;
    private ForgeConfigSpec.BooleanValue AFFECT_AGE_OF_EXILE_EXP;

    public ForgeConfigSpec.DoubleValue MIN_PLAYER_DMG_TO_GET_LOOT;

    public boolean affects(ModAction action) {

        if (action == ModAction.AOE_EXP) {
            return AFFECT_AGE_OF_EXILE_EXP.get();
        }
        if (action == ModAction.AOE_LOOT) {
            return AFFECT_AGE_OF_EXILE_LOOT.get();
        }
        if (action == ModAction.VANILLA_LOOT) {
            return AFFECT_VANILLA_LOOT_TABLES.get();
        }

        System.out.print("No enum: " + action.name());

        return false;
    }

    public boolean playerDidEnoughDamageTo(LivingEntity entity, ModAction action) {

        if (!affects(action)) {
            return true;
        }


        float damageDealt = EntityInfoComponent.get(entity)
                .getDamageStats()
                .getTotalPlayerDamage();

        if (damageDealt > 0) {

            float damageNeeded = entity.getMaxHealth() * MIN_PLAYER_DMG_TO_GET_LOOT.get()
                    .floatValue();

            return damageDealt >= damageNeeded;
        } else {// if its one shotted by player
            return EntityInfoComponent.get(entity)
                    .getDamageStats()
                    .getEnviroOrMobDmg() <= entity.getMaxHealth() / 2F;

        }
    }

    public boolean isDimensionExcluded(Level world) {
        String dimId = world.dimension()
                .location()
                .toString();
        return CheeseConfig.get().DIMENSIONS_EXCLUDED.get()
                .contains(dimId);

    }

    static String GATEWAYS = "gateways.owner";

    public boolean entityCounts(LivingEntity mob) {

        String id = mob.level().registryAccess().registry(Registries.ENTITY_TYPE).get().getKey(mob.getType())
                .toString();

        if (ENTITIES_EXCLUDED.get()
                .contains(id)) {
            return false;
        }

        if (!AFFECT_PEACEFUL_ANIMALS.get() && mob.getType()
                .getCategory()
                .isFriendly()) {
            return false;
        }

        // if it's spawned by a gateway
        if (mob.getPersistentData().hasUUID(GATEWAYS)) {
            return false;
        }

        return true;
    }
}
