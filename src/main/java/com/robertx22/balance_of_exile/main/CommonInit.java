package com.robertx22.balance_of_exile.main;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.balance_of_exile.anti_mob_farm.OnMobDeath;
import com.robertx22.balance_of_exile.anti_mob_farm.WorldTickMinute;
import com.robertx22.balance_of_exile.configs.BalanceConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import com.robertx22.library_of_exile.components.MySpawnReason;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class CommonInit implements ModInitializer {

    @Override
    public void onInitialize() {
        Components.INSTANCE = new Components();
        AutoConfig.register(BalanceConfig.class, JanksonConfigSerializer::new);

        ServerTickEvents.END_WORLD_TICK.register(new WorldTickMinute());

        ExileEvents.MOB_DEATH.register(new OnMobDeath());

        ExileEvents.SETUP_LOOT_CHANCE.register(new EventConsumer<ExileEvents.OnSetupLootChance>() {
            @Override
            public void accept(ExileEvents.OnSetupLootChance event) {
                if (BalanceConfig.get().GLOBAL_AFFECTS_WHAT.affects(ModAction.AOE_LOOT)) {

                    if (event.mobKilled != null) {

                        if (!BalanceConfig.get().ANTI_ENVIRO_DMG.playerDidEnoughDamageTo(event.mobKilled, ModAction.AOE_LOOT)) {
                            event.lootChance = 0;
                        }

                        if (BalanceConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
                            if (BalanceConfig.get().ANTI_MOB_FARM.AFFECTS_WHAT.affects(ModAction.AOE_LOOT)) {
                                float multi = AntiMobFarmCap.get(event.mobKilled.world)
                                    .getDropMultiForMob(event.mobKilled);
                                event.lootChance *= multi;
                            }
                        }
                        if (BalanceConfig.get().ANTI_SPAWNER.ENABLE_ANTI_SPAWNER) {
                            if (EntityInfoComponent.get(event.mobKilled)
                                .getSpawnReason() == MySpawnReason.SPAWNER) {
                                event.lootChance *= BalanceConfig.get().ANTI_SPAWNER.AGE_OF_EXILE_LOOT_MULTI;
                            }
                        }
                    }

                }
            }
        });

        ExileEvents.MOB_EXP_DROP.register(new EventConsumer<ExileEvents.OnMobExpDrop>() {
            @Override
            public void accept(ExileEvents.OnMobExpDrop event) {
                if (BalanceConfig.get().GLOBAL_AFFECTS_WHAT.affects(ModAction.AOE_EXP)) {
                    if (event.mobKilled != null) {

                        if (!BalanceConfig.get().ANTI_ENVIRO_DMG.playerDidEnoughDamageTo(event.mobKilled, ModAction.AOE_EXP)) {
                            event.exp = 0;
                        }
                        if (BalanceConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
                            if (BalanceConfig.get().ANTI_MOB_FARM.AFFECTS_WHAT.affects(ModAction.AOE_EXP)) {
                                float multi = AntiMobFarmCap.get(event.mobKilled.world)
                                    .getDropMultiForMob(event.mobKilled);
                                event.exp *= multi;
                            }
                        }
                        if (BalanceConfig.get().ANTI_SPAWNER.ENABLE_ANTI_SPAWNER) {
                            if (EntityInfoComponent.get(event.mobKilled)
                                .getSpawnReason() == MySpawnReason.SPAWNER) {
                                event.exp *= BalanceConfig.get().ANTI_SPAWNER.AGE_OF_EXILE_EXP_MULTI;
                            }
                        }
                    }
                }
            }
        });

        System.out.println("Balance of Exile loaded.");
    }

}
