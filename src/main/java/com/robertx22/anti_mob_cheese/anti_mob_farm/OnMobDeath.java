package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class OnMobDeath extends EventConsumer<ExileEvents.OnMobDeath> {


    @Override
    public void accept(ExileEvents.OnMobDeath event) {

        if (CheeseConfig.get().entityCounts(event.mob)) {

            Entity killer = EntityInfoComponent.get(event.mob).getDamageStats().getHighestDamager((ServerLevel) event.mob.level());

            if (killer instanceof Player) {
                AntiMobFarmCap.get(event.mob.level()).onValidMobDeathByPlayer(event.mob);
            }
        }
    }
}
