package com.robertx22.anti_mob_cheese.anti_mob_farm;

import net.minecraft.server.level.ServerLevel;

public class WorldTickMinute {

    static int ticks = 0;
    

    public static void onEndTick(ServerLevel serverWorld) {
        ticks++;

        if (ticks > 20 * 60) {
            ticks = 0;
            AntiMobFarmCap.get(serverWorld)
                    .onMinutePassed();
        }
    }
}
