package com.robertx22.anti_mob_cheese.main;

import com.robertx22.anti_mob_cheese.anti_mob_farm.AntiMobFarmCap;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.minecraft.util.Identifier;

public class Components {

    public static Components INSTANCE;

    public ComponentType<AntiMobFarmCap.IAntiMobFarmData> ANTI_MOB_FARM =
        ComponentRegistry.INSTANCE.registerIfAbsent(
            new Identifier("anti_mob_cheese", "anti_mob_farm"),
            AntiMobFarmCap.IAntiMobFarmData.class)
            .attach(WorldComponentCallback.EVENT, x -> new AntiMobFarmCap.DefaultImpl());

}
