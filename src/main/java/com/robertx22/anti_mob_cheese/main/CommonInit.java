package com.robertx22.anti_mob_cheese.main;

import com.robertx22.anti_mob_cheese.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.anti_mob_cheese.anti_mob_farm.OnMobDeath;
import com.robertx22.anti_mob_cheese.anti_mob_farm.WorldTickMinute;
import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import com.robertx22.library_of_exile.components.MySpawnReason;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.math.ChunkPos;

public class CommonInit implements ModInitializer {

    @Override
    public void onInitialize() {
        Components.INSTANCE = new Components();
        AutoConfig.register(CheeseConfig.class, JanksonConfigSerializer::new);

        ServerTickEvents.END_WORLD_TICK.register(new WorldTickMinute());

        ExileEvents.MOB_DEATH.register(new OnMobDeath());

        ExileEvents.ON_CHEST_LOOTED.register(new EventConsumer<ExileEvents.OnChestLooted>() {
            @Override
            public void accept(ExileEvents.OnChestLooted event) {
                AntiMobFarmCap.get(event.player.world)
                    .onLootChestOpened(new ChunkPos(event.pos));
            }
        });

        ExileEvents.SETUP_LOOT_CHANCE.register(new EventConsumer<ExileEvents.OnSetupLootChance>() {
            @Override
            public void accept(ExileEvents.OnSetupLootChance event) {
                if (CheeseConfig.get().GLOBAL_AFFECTS_WHAT.affects(ModAction.AOE_LOOT)) {

                    if (event.mobKilled != null) {

                        if (!CheeseConfig.get().ANTI_ENVIRO_DMG.playerDidEnoughDamageTo(event.mobKilled, ModAction.AOE_LOOT)) {
                            event.lootChance = 0;
                        }

                        if (CheeseConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
                            if (CheeseConfig.get().ANTI_MOB_FARM.AFFECTS_WHAT.affects(ModAction.AOE_LOOT)) {
                                float multi = AntiMobFarmCap.get(event.mobKilled.world)
                                    .getDropMultiForMob(event.mobKilled);
                                event.lootChance *= multi;
                            }
                        }
                        if (CheeseConfig.get().ANTI_SPAWNER.ENABLE_ANTI_SPAWNER) {
                            if (EntityInfoComponent.get(event.mobKilled)
                                .getSpawnReason() == MySpawnReason.SPAWNER) {
                                event.lootChance *= CheeseConfig.get().ANTI_SPAWNER.AGE_OF_EXILE_LOOT_MULTI;
                            }
                        }
                    }

                }
            }
        });

        ExileEvents.MOB_EXP_DROP.register(new EventConsumer<ExileEvents.OnMobExpDrop>() {
            @Override
            public void accept(ExileEvents.OnMobExpDrop event) {
                if (CheeseConfig.get().GLOBAL_AFFECTS_WHAT.affects(ModAction.AOE_EXP)) {
                    if (event.mobKilled != null) {

                        if (!CheeseConfig.get().ANTI_ENVIRO_DMG.playerDidEnoughDamageTo(event.mobKilled, ModAction.AOE_EXP)) {
                            event.exp = 0;
                        }
                        if (CheeseConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
                            if (CheeseConfig.get().ANTI_MOB_FARM.AFFECTS_WHAT.affects(ModAction.AOE_EXP)) {
                                float multi = AntiMobFarmCap.get(event.mobKilled.world)
                                    .getDropMultiForMob(event.mobKilled);
                                event.exp *= multi;
                            }
                        }
                        if (CheeseConfig.get().ANTI_SPAWNER.ENABLE_ANTI_SPAWNER) {
                            if (EntityInfoComponent.get(event.mobKilled)
                                .getSpawnReason() == MySpawnReason.SPAWNER) {
                                event.exp *= CheeseConfig.get().ANTI_SPAWNER.AGE_OF_EXILE_EXP_MULTI;
                            }
                        }
                    }
                }
            }
        });

        System.out.println("Anti Mob Cheese loaded.");
    }

}
