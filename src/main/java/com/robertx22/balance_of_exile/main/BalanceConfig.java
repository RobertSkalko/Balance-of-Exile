package com.robertx22.balance_of_exile.main;

import com.robertx22.balance_of_exile.anti_enviro_dmg.AntiEnviroDmgConfig;
import com.robertx22.balance_of_exile.anti_spawner.AntiSpawnerConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@me.sargunvohra.mcmods.autoconfig1u.annotation.Config(name = "balance_of_exile")
public class BalanceConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public AntiMobFarmConfig ANTI_MOB_FARM = new AntiMobFarmConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AntiSpawnerConfig ANTI_SPAWNER = new AntiSpawnerConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AntiEnviroDmgConfig ANTI_ENVIRO_DMG = new AntiEnviroDmgConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AffectsWhatConfig GLOBAL_AFFECTS_WHAT = new AffectsWhatConfig();

    public boolean AFFECT_PEACEFUL_ANIMALS = false;

    @Comment("Every feature is disabled in these dimensions")
    public List<String> DIMENSIONS_EXCLUDED = new ArrayList<>();
    public List<String> ENTITIES_EXCLUDED = new ArrayList<>();

    public static BalanceConfig get() {
        return AutoConfig.getConfigHolder(BalanceConfig.class)
            .getConfig();
    }

    public boolean isDimensionExcluded(World world) {
        String dimId = world.getRegistryKey()
            .getValue()
            .toString();
        return BalanceConfig.get().DIMENSIONS_EXCLUDED.contains(dimId);

    }

    public boolean entityCounts(LivingEntity mob) {

        String id = Registry.ENTITY_TYPE.getId(mob.getType())
            .toString();

        if (ENTITIES_EXCLUDED.contains(id)) {
            return false;
        }

        if (!AFFECT_PEACEFUL_ANIMALS && mob.getType()
            .getSpawnGroup()
            .isPeaceful()) {
            return false;
        }

        return true;
    }
}
