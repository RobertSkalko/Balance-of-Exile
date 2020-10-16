package com.robertx22.balance_of_exile.main;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.balance_of_exile.anti_mob_farm.OnMobDeath;
import com.robertx22.balance_of_exile.anti_mob_farm.WorldTickMinute;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import com.robertx22.library_of_exile.components.MySpawnReason;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;

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
                if (BalanceConfig.get().AFFECT_AGE_OF_EXILE_LOOT) {

                    if (event.mobKilled != null) {

                        if (!playerDidEnoughDamageTo(event.mobKilled)) {
                            event.lootChance = 0;
                        }

                        if (BalanceConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
                            float multi = AntiMobFarmCap.get(event.mobKilled.world)
                                .getDropMultiForMob(event.mobKilled);
                            event.lootChance *= multi;
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
                if (BalanceConfig.get().AFFECT_AGE_OF_EXILE_LOOT) {
                    if (event.mobKilled != null) {

                        if (!playerDidEnoughDamageTo(event.mobKilled)) {
                            event.exp = 0;
                        }
                        if (BalanceConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
                            float multi = AntiMobFarmCap.get(event.mobKilled.world)
                                .getDropMultiForMob(event.mobKilled);
                            event.exp *= multi;
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

    public static boolean playerDidEnoughDamageTo(LivingEntity entity) {

        float damageDealt = EntityInfoComponent.get(entity)
            .getDamageStats()
            .getTotalPlayerDamage();

        if (damageDealt > 0) {

            float damageNeeded = entity.getMaxHealth() * BalanceConfig.get().MIN_PLAYER_DMG_TO_GET_LOOT;

            return damageDealt >= damageNeeded;
        } else {// if its one shotted by player
            return EntityInfoComponent.get(entity)
                .getDamageStats()
                .getEnviroOrMobDmg() <= entity.getMaxHealth() / 2F;

        }
    }

}
