package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.library_of_exile.components.forge.BaseProvider;
import com.robertx22.library_of_exile.components.forge.BaseStorage;
import com.robertx22.library_of_exile.components.forge.ICommonCap;
import com.robertx22.library_of_exile.main.Ref;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkCap implements ICommonCap {

    @CapabilityInject(ChunkCap.class)
    public static final Capability<ChunkCap> Data = null;

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onEntityConstruct(AttachCapabilitiesEvent<Chunk> event) {
            event.addCapability(RESOURCE, new Provider(event.getObject()));
        }
    }

    public static final ResourceLocation RESOURCE = new ResourceLocation(Ref.MODID, "chunk_data");

    public static ChunkCap get(Chunk provider) {
        return provider.getCapability(Data)
            .orElse(new ChunkCap(provider));
    }

    public static class Provider extends BaseProvider<ChunkCap, Chunk> {
        public Provider(Chunk owner) {
            super(owner);
        }

        @Override
        public ChunkCap newDefaultImpl(Chunk owner) {
            return new ChunkCap(owner);
        }

        @Override
        public Capability<ChunkCap> dataInstance() {
            return Data;
        }
    }

    public static class Storage implements BaseStorage<ChunkCap> {

    }

    static String DATA_LOC = "fr";

    private int freeKills = CheeseConfig.get().FREE_MOB_KILLS_BEFORE_PENALTY_STARTS.get();

    Chunk chunk;

    public ChunkCap(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public CompoundNBT saveToNBT() {

        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt(DATA_LOC, freeKills);

        return nbt;
    }

    @Override
    public void loadFromNBT(CompoundNBT nbt) {
        this.freeKills = nbt.getInt(DATA_LOC);
    }

    public void onLootChestOpened() {
        this.freeKills += CheeseConfig.get().ADD_FREE_KILLS_ON_CHEST_LOOT.get();
    }

    public void onValidMobDeathByPlayer(LivingEntity en) {
        freeKills--;
    }

    public boolean isKillFree() {
        return freeKills > 0;
    }
}
