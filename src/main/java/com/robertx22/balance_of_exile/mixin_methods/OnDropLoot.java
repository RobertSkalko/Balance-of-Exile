package com.robertx22.balance_of_exile.mixin_methods;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.balance_of_exile.main.BalanceConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnDropLoot {

    public static void tryCancel(LivingEntity entity, CallbackInfo ci) {

        if (!BalanceConfig.get().AFFECT_MOB_LOOT_TABLES) {
            return;
        }
        if (BalanceConfig.get()
            .isDimensionExcluded(entity.world)) {
            return;
        }

        float dmg = EntityInfoComponent.get(entity)
            .getDamageStats()
            .getTotalPlayerDamage();

        if (dmg < entity.getMaxHealth() * BalanceConfig.get().MIN_PLAYER_DMG_TO_GET_LOOT) {
            ci.cancel();
            return;
        }

        if (BalanceConfig.get().ANTI_MOB_FARM.ENABLE_ANTI_MOB_FARM) {
            float multi = AntiMobFarmCap.get(entity.world)
                .getDropMultiForMob(entity);
            float chance = Math.abs((multi * 100) - 100);

            if (chance > entity.getRandom()
                .nextFloat() * 100) {
                ci.cancel();
                return;
            }
        }
    }

}
