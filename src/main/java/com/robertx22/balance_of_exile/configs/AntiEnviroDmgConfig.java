package com.robertx22.balance_of_exile.configs;

import com.robertx22.balance_of_exile.main.ModAction;
import com.robertx22.library_of_exile.components.EntityInfoComponent;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.entity.LivingEntity;

public class AntiEnviroDmgConfig {

    @Comment("This stops players cheesing mobs with lava for example. Pretty good way to fight mob farms unless mods have fake player damage traps.")
    public float MIN_PLAYER_DMG_TO_GET_LOOT = 0.5F;

    @ConfigEntry.Gui.CollapsibleObject
    AffectsWhatConfig AFFECTS_WHAT = new AffectsWhatConfig();

    public boolean playerDidEnoughDamageTo(LivingEntity entity, ModAction action) {

        if (!AFFECTS_WHAT.affects(action)) {
            return true;
        }

        float damageDealt = EntityInfoComponent.get(entity)
            .getDamageStats()
            .getTotalPlayerDamage();

        if (damageDealt > 0) {

            float damageNeeded = entity.getMaxHealth() * MIN_PLAYER_DMG_TO_GET_LOOT;

            return damageDealt >= damageNeeded;
        } else {// if its one shotted by player
            return EntityInfoComponent.get(entity)
                .getDamageStats()
                .getEnviroOrMobDmg() <= entity.getMaxHealth() / 2F;

        }
    }
}
