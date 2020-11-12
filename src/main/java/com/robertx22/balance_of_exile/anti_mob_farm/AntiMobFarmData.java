package com.robertx22.balance_of_exile.anti_mob_farm;

import com.robertx22.balance_of_exile.configs.BalanceConfig;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;

@Storable
public class AntiMobFarmData {

    @Store
    private HashMap<String, AntiMobFarmChunkData> map = new HashMap<String, AntiMobFarmChunkData>();

    public void onValidMobDeathByPlayer(LivingEntity en) {

        if (en instanceof SlimeEntity) {
            if (en.world.random.nextFloat() >= 0.2F) {
                return; // slimes get a slight rule relaxation
            }
        }

        String key = getKey(en);

        AntiMobFarmChunkData data = map.getOrDefault(key, new AntiMobFarmChunkData());
        data.onMobDeath();
        map.put(key, data);
    }

    public void onNewLootChestOpened(ChunkPos cp) {
        String key = getKey(cp);
        AntiMobFarmChunkData data = map.getOrDefault(key, new AntiMobFarmChunkData());
        data.onLootChestOpened();
        map.put(key, data);
    }

    public float getDropMultiForMob(LivingEntity en) {

        if (BalanceConfig.get()
            .isDimensionExcluded(en.world)) {
            return 1;
        }

        String key = getKey(en);

        if (map.containsKey(key)) {
            return map.get(key)
                .getDropsMulti();
        } else {
            return 1;
        }
    }

    public void tickDownAllKillCounters() {
        map.entrySet()
            .forEach(x -> {
                x.getValue()
                    .tickDown();
            });

        map.entrySet()
            .removeIf(x -> x.getValue()
                .getDropsMulti() == 1); // dont need to save full ones.
        // should reduce file size on super old worlds
    }

    private String getKey(LivingEntity en) {
        return getKey(new ChunkPos(EntityInfoComponent.get(en)
            .getSpawnPos()));
    }

    private String getKey(ChunkPos cp) {
        return cp.x + "_" + cp.z;
    }
}