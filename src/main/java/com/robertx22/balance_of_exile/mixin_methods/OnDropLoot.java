package com.robertx22.balance_of_exile.mixin_methods;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.balance_of_exile.main.BalanceConfig;
import com.robertx22.balance_of_exile.main.ModAction;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnDropLoot {

    public static void tryCancel(LivingEntity entity, CallbackInfo ci) {

        if (!BalanceConfig.get().GLOBAL_AFFECTS_WHAT.affects(ModAction.VANILLA_LOOT)) {
            return;
        }
        if (BalanceConfig.get()
            .isDimensionExcluded(entity.world)) {
            return;
        }
        if (!BalanceConfig.get().ANTI_ENVIRO_DMG.playerDidEnoughDamageTo(entity, ModAction.VANILLA_LOOT)) {
            ci.cancel();
            return;
        }
        if (!BalanceConfig.get()
            .entityCounts(entity)) {
            return;
        }

        if (BalanceConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
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
