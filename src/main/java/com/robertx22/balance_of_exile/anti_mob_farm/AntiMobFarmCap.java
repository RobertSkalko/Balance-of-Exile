package com.robertx22.balance_of_exile.anti_mob_farm;

import com.robertx22.balance_of_exile.main.Components;
import com.robertx22.library_of_exile.utils.LoadSave;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AntiMobFarmCap {

    public static final Identifier RESOURCE = new Identifier("balance_of_exile", "anti_mob_farm");

    public static AntiMobFarmCap.IAntiMobFarmData get(World provider) {
        return Components.INSTANCE.ANTI_MOB_FARM.get(provider);
    }

    public interface IAntiMobFarmData extends Component {

        void onValidMobDeathByPlayer(LivingEntity en);

        float getDropMultiForMob(LivingEntity en);

        void onMinutePassed();

    }

    static String DATA_LOC = "balance_of_exile:data";

    public static class DefaultImpl implements IAntiMobFarmData {

        AntiMobFarmData data = new AntiMobFarmData();

        @Override
        public CompoundTag toTag(CompoundTag nbt) {

            if (data != null) {
                LoadSave.Save(data, nbt, DATA_LOC);
            }

            return nbt;

        }

        @Override
        public void fromTag(CompoundTag nbt) {

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
    }

}
