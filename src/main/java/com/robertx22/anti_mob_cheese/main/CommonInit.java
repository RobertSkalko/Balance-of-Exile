package com.robertx22.anti_mob_cheese.main;

import com.robertx22.anti_mob_cheese.anti_mob_farm.AntiMobFarmCap;
import com.robertx22.anti_mob_cheese.anti_mob_farm.OnMobDeath;
import com.robertx22.anti_mob_cheese.anti_mob_farm.WorldTickMinute;
import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.anti_mob_cheese.mixin_methods.OnDropLoot;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import com.robertx22.library_of_exile.main.ForgeEvents;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AMC.MODID)
public class CommonInit {

    public CommonInit() {
        final IEventBus bus = FMLJavaModLoadingContext.get()
            .getModEventBus();

        bus.addListener(this::commonSetupEvent);

        ForgeEvents.registerForgeEvent(LivingDropsEvent.class, event -> {
            if (OnDropLoot.tryCancel(event.getEntityLiving())) {
                event.setCanceled(true);
            }
        });

        ForgeEvents.registerForgeEvent(TickEvent.WorldTickEvent.class, event -> {

            if (event.world instanceof ServerWorld) {
                if (event.phase == TickEvent.Phase.END) {
                    WorldTickMinute.onEndTick((ServerWorld) event.world);
                }
            }

        });

        ExileEvents.MOB_DEATH.register(new OnMobDeath());

        ExileEvents.ON_CHEST_LOOTED.register(new EventConsumer<ExileEvents.OnChestLooted>() {
            @Override
            public void accept(ExileEvents.OnChestLooted event) {
                AntiMobFarmCap.get(event.player.level)
                    .onLootChestOpened(new ChunkPos(event.pos));
            }
        });

        ExileEvents.SETUP_LOOT_CHANCE.register(new EventConsumer<ExileEvents.OnSetupLootChance>() {
            @Override
            public void accept(ExileEvents.OnSetupLootChance event) {
                if (CheeseConfig.get()
                    .affects(ModAction.AOE_LOOT)) {

                    if (event.mobKilled != null) {

                        if (!CheeseConfig.get()
                            .playerDidEnoughDamageTo(event.mobKilled, ModAction.AOE_LOOT)) {
                            event.lootChance = 0;
                        }

                        if (CheeseConfig.get().ENABLE_ANTI_MOB_FARM.get()) {
                            if (CheeseConfig.get()
                                .affects(ModAction.AOE_LOOT)) {
                                float multi = AntiMobFarmCap.get(event.mobKilled.level)
                                    .getDropMultiForMob(event.mobKilled);
                                event.lootChance *= multi;
                            }
                        }

                    }

                }
            }
        });

        ExileEvents.MOB_EXP_DROP.register(new EventConsumer<ExileEvents.OnMobExpDrop>() {
            @Override
            public void accept(ExileEvents.OnMobExpDrop event) {
                if (CheeseConfig.get()
                    .affects(ModAction.AOE_EXP)) {
                    if (event.mobKilled != null) {

                        if (!CheeseConfig.get()
                            .playerDidEnoughDamageTo(event.mobKilled, ModAction.AOE_EXP)) {
                            event.exp = 0;
                        }
                        if (CheeseConfig.get().ENABLE_ANTI_MOB_FARM.get()) {
                            if (CheeseConfig.get()
                                .affects(ModAction.AOE_EXP)) {
                                float multi = AntiMobFarmCap.get(event.mobKilled.level)
                                    .getDropMultiForMob(event.mobKilled);
                                event.exp *= multi;
                            }
                        }

                    }
                }
            }
        });

        System.out.println("Anti Mob Cheese loaded.");
    }

    public void commonSetupEvent(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(
            AntiMobFarmCap.class,
            new AntiMobFarmCap.Storage(),
            () -> new AntiMobFarmCap(null)
        );
    }

}
