package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.main.AMC;
import com.robertx22.library_of_exile.components.ICap;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AntiMobFarmCap implements ICap {

    public static final ResourceLocation RESOURCE = new ResourceLocation(AMC.MODID, "world_data");
    public static Capability<AntiMobFarmCap> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    transient final LazyOptional<AntiMobFarmCap> supp = LazyOptional.of(() -> this);

    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == INSTANCE) {
            return supp.cast();
        }
        return LazyOptional.empty();

    }

    public static AntiMobFarmCap get(Level provider) {
        return provider.getCapability(INSTANCE)
                .orElse(null);
    }


    static String DATA_LOC = "anti_mob_cheese:data";

    AntiMobFarmData data = new AntiMobFarmData();

    Level world;

    public AntiMobFarmCap(Level world) {
        this.world = world;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (data != null) {
            LoadSave.Save(data, nbt, DATA_LOC);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data = LoadSave.Load(AntiMobFarmData.class, new AntiMobFarmData(), nbt, DATA_LOC);

        if (data == null) {
            data = new AntiMobFarmData();
        }
    }

    public void onValidMobDeathByPlayer(LivingEntity en) {
        this.data.onValidMobDeathByPlayer(en);
    }

    public float getDropMultiForMob(LivingEntity en) {
        return this.data.getDropMultiForMob(en);
    }

    public void onMinutePassed() {
        this.data.tickDownAllKillCounters();
    }

    public void onLootChestOpened(ChunkPos pos) {
        this.data.onNewLootChestOpened(pos);
    }

    @Override
    public String getCapIdForSyncing() {
        return "world_antimobfarm";
    }


}
