package com.robertx22.balance_of_exile.anti_mob_farm;

import com.robertx22.balance_of_exile.configs.BalanceConfig;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.util.math.MathHelper;

@Storable
public class AntiMobFarmChunkData {

    @Store
    private float p = 1;

    public void tickDown() {
        this.p += BalanceConfig.get().ANTI_MOB_FARM.ON_MINUTE_PASSED_INCREASE_BY;
        clamp();
    }

    public void onMobDeath() {
        this.p = p - BalanceConfig.get().ANTI_MOB_FARM.ON_MOB_KILLED_DECREASE_BY;

        if (p < 0.5F) {
            this.p = p - BalanceConfig.get().ANTI_MOB_FARM.ON_MOB_KILLED_DECREASE_BY; // if it's likely a mob farm, decrease faster
        }

        clamp();
    }

    public void onLootChestOpened() {
        this.p = 1;
        clamp();
    }

    public float getDropsMulti() {
        return MathHelper.clamp(p, 0, 1);
    }

    public void clamp() {
        this.p = MathHelper.clamp(p, 0, 1);
    }
}
