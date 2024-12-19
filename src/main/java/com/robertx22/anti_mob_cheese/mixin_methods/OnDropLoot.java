package com.robertx22.anti_mob_cheese.mixin_methods;

import com.robertx22.anti_mob_cheese.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.anti_mob_cheese.main.ModAction;
import net.minecraft.world.entity.LivingEntity;

public class OnDropLoot {


    public static boolean tryCancel(LivingEntity entity, ModAction action) {

        if (!CheeseConfig.get().affects(action)) {
            return false;
        }
        if (CheeseConfig.get().isDimensionExcluded(entity.level())) {
            return false;
        }
        if (!CheeseConfig.get().playerDidEnoughDamageTo(entity, action)) {
            return true;
        }
        if (!CheeseConfig.get().entityCounts(entity)) {
            return false;
        }

        if (CheeseConfig.get().ENABLE_ANTI_MOB_FARM.get()) {

            if (CheeseConfig.get().affects(action)) {
                float multi = AntiMobFarmCap.get(entity.level()).getDropMultiForMob(entity);
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
