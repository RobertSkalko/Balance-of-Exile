package com.robertx22.balance_of_exile.main;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.minecraft.util.Identifier;

public class Components {

    public static Components INSTANCE;

    public ComponentType<AntiMobFarmCap.IAntiMobFarmData> ANTI_MOB_FARM =
    ComponentRegistry.INSTANCE.registerIfAbsent(
        new Identifier("balance_of_exile", "anti_mob_farm"),
        AntiMobFarmCap.IAntiMobFarmData.class)
        .attach(WorldComponentCallback.EVENT, x -> new AntiMobFarmCap.DefaultImpl());

}
