package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.robertx22.anti_mob_cheese.main.Components;
import com.robertx22.library_of_exile.utils.LoadSave;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class AntiMobFarmCap {

    public static final Identifier RESOURCE = new Identifier("anti_mob_cheese", "anti_mob_farm");

    public static AntiMobFarmCap.IAntiMobFarmData get(World provider) {
        return Components.INSTANCE.ANTI_MOB_FARM.get(provider);
    }

    public interface IAntiMobFarmData extends Component {

        void onValidMobDeathByPlayer(LivingEntity en);

        float getDropMultiForMob(LivingEntity en);

        void onMinutePassed();

        void onLootChestOpened(ChunkPos pos);

    }

    static String DATA_LOC = "anti_mob_cheese:data";

    public static class DefaultImpl implements IAntiMobFarmData {

        AntiMobFarmData data = new AntiMobFarmData();

        @Override
        public NbtCompound toTag(NbtCompound nbt) {

            if (data != null) {
                LoadSave.Save(data, nbt, DATA_LOC);
            }

            return nbt;

        }

        @Override
        public void fromTag(NbtCompound nbt) {

            data = LoadSave.Load(AntiMobFarmData.class, new AntiMobFarmData(), nbt, DATA_LOC);

            if (data == null) {
                data = new AntiMobFarmData();
            }

        }

        @Override
        public void onValidMobDeathByPlayer(LivingEntity en) {
            this.data.onValidMobDeathByPlayer(en);
        }

        @Override
        public float getDropMultiForMob(LivingEntity en) {
            return this.data.getDropMultiForMob(en);
        }

        @Override
        public void onMinutePassed() {
            this.data.tickDownAllKillCounters();
        }

        @Override
        public void onLootChestOpened(ChunkPos pos) {
            this.data.onNewLootChestOpened(pos);
        }
    }

}
