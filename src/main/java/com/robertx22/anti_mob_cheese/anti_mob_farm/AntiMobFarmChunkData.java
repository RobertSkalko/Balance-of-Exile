package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.util.math.MathHelper;

@Storable
public class AntiMobFarmChunkData {

    @Store
    private float p = 1;
    // free kills
    @Store
    private int m = CheeseConfig.get().ANTI_MOB_FARM.FREE_MOB_KILLS_BEFORE_PENALTY_STARTS;

    public void tickDown() {
        this.p += CheeseConfig.get().ANTI_MOB_FARM.ON_MINUTE_PASSED_INCREASE_BY;
        clamp();
    }

    public void onMobDeath() {

        if (m > 0) {
            m--;
            return;
        }

        this.p = p - CheeseConfig.get().ANTI_MOB_FARM.ON_MOB_KILLED_DECREASE_BY;

        if (p < 0.5F) {
            this.p = p - CheeseConfig.get().ANTI_MOB_FARM.ON_MOB_KILLED_DECREASE_BY; // if it's likely a mob farm, decrease faster
        }

        clamp();
    }

    public void onLootChestOpened() {
        this.p = 1;
        this.m += CheeseConfig.get().ANTI_MOB_FARM.ADD_FREE_KILLS_ON_CHEST_LOOT;
        clamp();
    }

    public float getDropsMulti() {
        return MathHelper.clamp(p, 0, 1);
    }

    public void clamp() {
        this.p = MathHelper.clamp(p, 0, 1);
    }
}
