package com.robertx22.anti_mob_cheese.anti_mob_farm;

import net.minecraft.world.server.ServerWorld;

public class WorldTickMinute {

    static int ticks = 0;

    public static void onEndTick(ServerWorld serverWorld) {
        ticks++;

        if (ticks > 20 * 60) {
            ticks = 0;
            AntiMobFarmCap.get(serverWorld)
                .onMinutePassed();
        }
    }
}
