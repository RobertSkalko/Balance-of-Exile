package com.robertx22.balance_of_exile.anti_mob_farm;

import com.robertx22.balance_of_exile.main.BalanceConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class OnMobDeath extends EventConsumer<ExileEvents.OnMobDeath> {

    @Override
    public void accept(ExileEvents.OnMobDeath event) {

        if (BalanceConfig.get()
            .entityCounts(event.mob)) {

            Entity killer = EntityInfoComponent.get(event.mob)
                .getDamageStats()
                .getHighestDamager((ServerWorld) event.mob.world);

            if (killer instanceof PlayerEntity) {
                AntiMobFarmCap.get(event.mob.world)
                    .onValidMobDeathByPlayer(event.mob);
            }
        }
    }
}
