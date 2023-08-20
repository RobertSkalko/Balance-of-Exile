package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import com.robertx22.anti_mob_cheese.main.AMC;
import com.robertx22.library_of_exile.components.ICap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class ChunkCap implements ICap {


    public static final ResourceLocation RESOURCE = new ResourceLocation(AMC.MODID, "chunk_data");
    public static Capability<ChunkCap> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    transient final LazyOptional<ChunkCap> supp = LazyOptional.of(() -> this);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == INSTANCE) {
            return supp.cast();
        }
        return LazyOptional.empty();

    }

    public static ChunkCap get(LevelChunk provider) {
        return provider.getCapability(INSTANCE)
                .orElse(null);
    }


    static String DATA_LOC = "fr";

    private int freeKills = CheeseConfig.get().FREE_MOB_KILLS_BEFORE_PENALTY_STARTS.get();

    LevelChunk chunk;

    public ChunkCap(LevelChunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt(DATA_LOC, freeKills);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
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

    @Override
    public String getCapIdForSyncing() {
        return "chunk_data_antimobfarm";
    }


}
