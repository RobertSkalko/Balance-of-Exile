package com.robertx22.balance_of_exile.main;

import com.robertx22.balance_of_exile.temp_spawners.TempSpawnerComponent;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.util.Identifier;

public class ComponentInit implements BlockComponentInitializer {

    public static Identifier SPAWNER_ID = new Identifier("balance_of_exile", "spawner");

    public static final ComponentKey<TempSpawnerComponent> SPAWNER =
        ComponentRegistryV3.INSTANCE.getOrCreate(SPAWNER_ID, TempSpawnerComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry reg) {
        reg.beginRegistration(MobSpawnerBlockEntity.class, SPAWNER)
            .impl(TempSpawnerComponent.class)
            .end(block -> new TempSpawnerComponent(block));

    }

}
