package com.robertx22.anti_mob_cheese.anti_mob_farm;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

public class WorldTickMinute implements ServerTickEvents.EndWorldTick {

    static int ticks = 0;

    @Override
    public void onEndTick(ServerWorld serverWorld) {
        ticks++;

        if (ticks > 20 * 60) {
            ticks = 0;
            AntiMobFarmCap.get(serverWorld)
                .onMinutePassed();
        }
    }
}
