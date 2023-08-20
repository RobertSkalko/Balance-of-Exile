package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import org.joml.Math;


public class AntiMobFarmChunkData {


    private float p = 1;

    public void tickDown() {
        this.p += CheeseConfig.get().ON_MINUTE_PASSED_INCREASE_BY.get();

        clamp();
    }

    public boolean canBeWipedFromData() {
        return getDropsMulti() == 1;
    }

    public void onMobDeath() {

        this.p = p - CheeseConfig.get().ON_MOB_KILLED_DECREASE_BY.get()
                .floatValue();

        if (p < 0.5F) {
            this.p = p - CheeseConfig.get().ON_MOB_KILLED_DECREASE_BY.get()
                    .floatValue(); // if it's likely a mob farm, decrease faster
        }

        clamp();
    }

    public void onLootChestOpened() {
        this.p = 1;
        clamp();
    }

    public float getDropsMulti() {
        return Math.clamp(p, 0, 1);
    }

    public void clamp() {
        this.p = Math.clamp(p, 0, 1);
    }
}
