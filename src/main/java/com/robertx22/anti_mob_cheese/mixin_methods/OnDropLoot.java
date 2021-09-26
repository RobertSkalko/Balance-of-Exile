package com.robertx22.anti_mob_cheese.mixin_methods;

import com.robertx22.anti_mob_cheese.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.anti_mob_cheese.main.ModAction;
import net.minecraft.entity.LivingEntity;

public class OnDropLoot {

    public static boolean tryCancel(LivingEntity entity) {

        if (!CheeseConfig.get()
            .affects(ModAction.VANILLA_LOOT)) {
            return false;
        }
        if (CheeseConfig.get()
            .isDimensionExcluded(entity.level)) {
            return false;
        }
        if (!CheeseConfig.get()
            .playerDidEnoughDamageTo(entity, ModAction.VANILLA_LOOT)) {
            return true;
        }
        if (!CheeseConfig.get()
            .entityCounts(entity)) {
            return false;
        }

        if (CheeseConfig.get().ENABLE_ANTI_MOB_FARM.get()) {

            if (CheeseConfig.get()
                .affects(ModAction.VANILLA_LOOT)) {
                float multi = AntiMobFarmCap.get(entity.level)
                    .getDropMultiForMob(entity);
                float chance = Math.abs((multi * 100F) - 100F);

                if (chance > entity.getRandom()
                    .nextFloat() * 100) {
                    return true;
                }
            }
        }

        return false;

    }

}
