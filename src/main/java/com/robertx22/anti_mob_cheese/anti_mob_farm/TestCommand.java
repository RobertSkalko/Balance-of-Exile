package com.robertx22.anti_mob_cheese.anti_mob_farm;

import com.mojang.brigadier.CommandDispatcher;
import com.robertx22.anti_mob_cheese.main.AMC;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import static net.minecraft.commands.Commands.literal;

public class TestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                literal(AMC.MODID)
                        .then(literal("chunk_info").requires(e -> e.hasPermission(2))
                                .executes(ctx -> run(ctx.getSource()))));
    }

    private static int run(CommandSourceStack source) {

        try {

            if (source.getEntity() instanceof ServerPlayer p) {
                var cp = new ChunkPos(p.blockPosition());
                p.sendSystemMessage(Component.literal(cp.toString() + ": Free Kills: " + ChunkCap.get(p.level().getChunkAt(p.blockPosition())).getFreeKills()
                        + ", Drop/Exp Multi: " + AntiMobFarmCap.get(p.level()).data.getDropMultiForChunk(cp) + "x"
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }
}
