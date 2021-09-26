package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.library_of_exile.components.forge.BaseProvider;
import com.robertx22.library_of_exile.components.forge.BaseStorage;
import com.robertx22.library_of_exile.components.forge.ICommonCap;
import com.robertx22.library_of_exile.main.Ref;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AntiMobFarmCap implements ICommonCap {

    @CapabilityInject(AntiMobFarmCap.class)
    public static final Capability<AntiMobFarmCap> Data = null;

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onEntityConstruct(AttachCapabilitiesEvent<World> event) {
            event.addCapability(RESOURCE, new Provider(event.getObject()));
        }
    }

    public static final ResourceLocation RESOURCE = new ResourceLocation(Ref.MODID, "chunk_data");

    public static AntiMobFarmCap get(World provider) {
        return provider.getCapability(Data)
            .orElse(new AntiMobFarmCap(provider));
    }

    public static class Provider extends BaseProvider<AntiMobFarmCap, World> {
        public Provider(World owner) {
            super(owner);
        }

        @Override
        public AntiMobFarmCap newDefaultImpl(World owner) {
            return new AntiMobFarmCap(owner);
        }

        @Override
        public Capability<AntiMobFarmCap> dataInstance() {
            return Data;
        }
    }

    public static class Storage implements BaseStorage<AntiMobFarmCap> {

    }

    static String DATA_LOC = "anti_mob_cheese:data";

    AntiMobFarmData data = new AntiMobFarmData();

    World world;

    public AntiMobFarmCap(World world) {
        this.world = world;
    }

    @Override
    public CompoundNBT saveToNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (data != null) {
            LoadSave.Save(data, nbt, DATA_LOC);
        }
        return nbt;
    }

    @Override
    public void loadFromNBT(CompoundNBT nbt) {
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

}
