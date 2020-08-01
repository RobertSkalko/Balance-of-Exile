package com.robertx22.balance_of_exile.anti_mass_kills;

import com.robertx22.balance_of_exile.main.BalanceConfig;
import net.minecraft.entity.Entity;

public class AntiMassKillsConfig {

    public boolean ENABLE_ANTI_MASS_KILLS = true;

    public int TIME_FRAME_IN_SECONDS = 5;

    public int LOCATION_IN_BLOCKS = 15;

    public int MOB_KILLS_IN_TIME_FRAME_NEEDED = 12;

    public boolean shouldPenalize(Entity en) {
        if (ENABLE_ANTI_MASS_KILLS) {
            int kills = OnMobDeathRecord.getKills(en);
            if (kills > BalanceConfig.get().ANTI_MASS_kILLS.MOB_KILLS_IN_TIME_FRAME_NEEDED) {
                return true;
            }
        }
        return false;
    }

}
