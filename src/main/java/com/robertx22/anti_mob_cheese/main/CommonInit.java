package com.robertx22.anti_mob_cheese.main;

import com.robertx22.anti_mob_cheese.anti_mob_farm.*;
import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.anti_mob_cheese.mixin_methods.OnDropLoot;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import com.robertx22.library_of_exile.main.ApiForgeEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

@Mod(AMC.MODID)
public class CommonInit {

    public CommonInit() {
        final IEventBus bus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        ModLoadingContext.get()
                .registerConfig(ModConfig.Type.COMMON, CheeseConfig.commonSpec);

        bus.addListener(this::commonSetupEvent);

        ApiForgeEvents.registerForgeEvent(LivingDropsEvent.class, event -> {
            if (event.getEntity() instanceof Player == false) {
                if (OnDropLoot.tryCancel(event.getEntity())) {
                    event.setCanceled(true);
                }
            }
        });

        ApiForgeEvents.registerForgeEvent(RegisterCommandsEvent.class, event -> {
            TestCommand.register(event.getDispatcher());
        });

        ApiForgeEvents.registerForgeEvent(TickEvent.LevelTickEvent.class, event -> {
            if (event.level instanceof ServerLevel) {
                if (event.phase == TickEvent.Phase.END) {
                    WorldTickMinute.onEndTick((ServerLevel) event.level);
                }
            }

        });

        ExileEvents.MOB_DEATH.register(new OnMobDeath());

        ExileEvents.ON_CHEST_LOOTED.register(new EventConsumer<ExileEvents.OnChestLooted>() {
            @Override
            public void accept(ExileEvents.OnChestLooted event) {
                try {
                    AntiMobFarmCap.get(event.player.level())
                            .onLootChestOpened(new ChunkPos(event.pos));
                    ChunkCap.get(event.player.level().getChunkAt(event.pos))
                            .onLootChestOpened();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                                float multi = AntiMobFarmCap.get(event.mobKilled.level())
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
                                float multi = AntiMobFarmCap.get(event.mobKilled.level())
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


        ApiForgeEvents.registerForgeEvent(RegisterCapabilitiesEvent.class, x -> {
            x.register(AntiMobFarmCap.class);
            x.register(ChunkCap.class);
        });

        MinecraftForge.EVENT_BUS.addGenericListener(Level.class, (Consumer<AttachCapabilitiesEvent<Level>>) x -> {
            x.addCapability(AntiMobFarmCap.RESOURCE, new AntiMobFarmCap(x.getObject()));
        });


        MinecraftForge.EVENT_BUS.addGenericListener(LevelChunk.class, (Consumer<AttachCapabilitiesEvent<LevelChunk>>) x -> {
            x.addCapability(ChunkCap.RESOURCE, new ChunkCap(x.getObject()));
        });
    }

}
