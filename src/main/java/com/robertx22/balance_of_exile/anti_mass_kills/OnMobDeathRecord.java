package com.robertx22.balance_of_exile.anti_mass_kills;

import com.robertx22.balance_of_exile.main.BalanceConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class OnMobDeathRecord extends EventConsumer<ExileEvents.OnMobDeath> {

    public static HashMap<Identifier, HashMap<String, HashMap<Integer, Integer>>> mainMap = new HashMap<>();

    static int mobCount = 0;

    @Override
    public void accept(ExileEvents.OnMobDeath event) {

        if (BalanceConfig.get()
            .entityCounts(event.mob)) {

            Entity killer = EntityInfoComponent.get(event.mob)
                .getDamageStats()
                .getHighestDamager((ServerWorld) event.mob.world);

            if (killer instanceof PlayerEntity) {

                mobCount++;

                Identifier dim = event.mob.world.getDimensionRegistryKey()
                    .getValue();

                int loc = BalanceConfig.get().ANTI_MASS_kILLS.LOCATION_IN_BLOCKS;
                ;

                if (!mainMap.containsKey(dim)) {
                    mainMap.put(dim, new HashMap<>());
                }

                HashMap<String, HashMap<Integer, Integer>> locMap = mainMap.get(dim);

                String place = event.mob.getPos().x / loc + "_" + event.mob.getPos().z / loc;

                if (!locMap.containsKey(place)) {
                    locMap.put(place, new HashMap<>());
                }

                int seconds = (int) (System.currentTimeMillis() / 1000);

                HashMap<Integer, Integer> timeframeMap = locMap.get(place);

                int timeframe = seconds / BalanceConfig.get().ANTI_MASS_kILLS.TIMEFRAME_IN_SECONDS;

                if (!timeframeMap.containsKey(timeframe)) {
                    timeframeMap.put(timeframe, 0);
                }
                timeframeMap.put(timeframe, timeframeMap.get(timeframe) + 1);
            }
        }

    }
}
