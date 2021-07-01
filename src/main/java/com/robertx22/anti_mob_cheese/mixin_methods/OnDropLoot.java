package com.robertx22.anti_mob_cheese.mixin_methods;

import com.robertx22.anti_mob_cheese.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.anti_mob_cheese.main.ModAction;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnDropLoot {

    public static void tryCancel(LivingEntity entity, CallbackInfo ci) {

        if (!CheeseConfig.get().GLOBAL_AFFECTS_WHAT.affects(ModAction.VANILLA_LOOT)) {
            return;
        }
        if (CheeseConfig.get()
            .isDimensionExcluded(entity.world)) {
            return;
        }
        if (!CheeseConfig.get().ANTI_ENVIRO_DMG.playerDidEnoughDamageTo(entity, ModAction.VANILLA_LOOT)) {
            ci.cancel();
            return;
        }
        if (!CheeseConfig.get()
            .entityCounts(entity)) {
            return;
        }

        if (CheeseConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {

            if (CheeseConfig.get().ANTI_MOB_FARM.AFFECTS_WHAT.affects(ModAction.VANILLA_LOOT)) {
                float multi = AntiMobFarmCap.get(entity.world)
                    .getDropMultiForMob(entity);
                float chance = Math.abs((multi * 100F) - 100F);

                if (chance > entity.getRandom()
                    .nextFloat() * 100) {
                    ci.cancel();
                    return;
                }
            }
        }

    }

}
