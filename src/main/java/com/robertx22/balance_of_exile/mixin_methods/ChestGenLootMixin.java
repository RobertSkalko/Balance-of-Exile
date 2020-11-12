package com.robertx22.balance_of_exile.mixin_methods;

import com.robertx22.balance_of_exile.anti_mob_farm.AntiMobFarmCap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ChestGenLootMixin {
    public static void onLootGen(Inventory inventory, LootContext context, CallbackInfo ci) {
        BlockEntity chest = null;
        BlockPos pos = null;

        if (inventory instanceof BlockEntity) {
            chest = (BlockEntity) inventory;
        }

        if (context.hasParameter(LootContextParameters.THIS_ENTITY) && context.get(LootContextParameters.THIS_ENTITY) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) context.get(LootContextParameters.THIS_ENTITY);

            World world = null;
            if (chest != null) {
                world = chest.getWorld();
                pos = chest.getPos();
            }

            if (world == null) {
                return;
            }

            if (inventory instanceof ChestBlockEntity) {
                AntiMobFarmCap.get(world)
                    .onLootChestOpened(new ChunkPos(pos));
            }

        }
    }

}
