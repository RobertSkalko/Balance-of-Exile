package com.robertx22.balance_of_exile.anti_spawner;

import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

public class AntiSpawnerConfig {

    @Comment("Enables anti mob spawner module.")
    public boolean ENABLE_ANTI_SPAWNER = true;

    @Comment("0 to 1. By default 0, meaning absolutely no loot or exp from spawners. 1 means normal.")
    public float AGE_OF_EXILE_LOOT_MULTI = 0;
    public float AGE_OF_EXILE_EXP_MULTI = 0;

}
