package com.robertx22.balance_of_exile.main;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.balance_of_exile.anti_mob_farm.OnMobDeath;
import com.robertx22.balance_of_exile.anti_mob_farm.WorldTickMinute;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
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
                    }
                }
            }
        });

        System.out.println("Balance of Exile loaded.");
    }

    static boolean playerDidEnoughDamageTo(LivingEntity entity) {
        return EntityInfoComponent.get(entity)
            .getDamageStats()
            .getTotalPlayerDamage() < entity.getMaxHealth() * BalanceConfig.get().MIN_PLAYER_DMG_TO_GET_LOOT;
    }

}