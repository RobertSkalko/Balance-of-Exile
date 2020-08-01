package com.robertx22.balance_of_exile.anti_mass_kills;

import com.robertx22.balance_of_exile.main.BalanceConfig;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class OnMobDeathRecord extends EventConsumer<ExileEvents.OnMobDeath> {

    public static HashMap<String, Integer> mainMap = new HashMap<>();

    public static int getKills(Entity en) {
        return mainMap.getOrDefault(getKey(en), 0);
    }

    public static String getKey(Entity entity) {

        Identifier dim = entity.world.getDimensionRegistryKey()
            .getValue();

        int loc = BalanceConfig.get().ANTI_MASS_kILLS.LOCATION_IN_BLOCKS;

        String place = (int) (entity.getPos().x / loc) + "_" + (int) (entity.getPos().z / loc);

        int seconds = (int) (System.currentTimeMillis() / 1000);

        int timeframe = seconds / BalanceConfig.get().ANTI_MASS_kILLS.TIME_FRAME_IN_SECONDS;

        return dim.toString() + place + timeframe;

    }

    @Override
    public void accept(ExileEvents.OnMobDeath event) {

        if (BalanceConfig.get()
            .entityCounts(event.mob)) {

            if (mainMap.size() > 1000) {
                mainMap.clear();
            }

            String key = getKey(event.mob);

            mainMap.put(key, mainMap.getOrDefault(key, 1) + 1);

        }

    }
}
