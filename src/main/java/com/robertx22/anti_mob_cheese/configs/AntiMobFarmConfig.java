package com.robertx22.anti_mob_cheese.configs;

import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

public class AntiMobFarmConfig {

    @Comment("This is a simple measure that makes mob farming a lot tougher. In essence, it reduces loot drops every time you kill a mob in the same chunk. The loot drops are regenerated to default over time. This means mob farms would get to 0 loot quickly, while normal player killings would not be affected much or at all.")
    public boolean ENABLE_ANTI_MOB_FARM = true;

    @ConfigEntry.Gui.CollapsibleObject
    public AffectsWhatConfig AFFECTS_WHAT = new AffectsWhatConfig();

    @Comment("Decreases loot drops by 2% by default every time a mob is killed")
    public float ON_MOB_KILLED_DECREASE_BY = 0.02F;
    public float ON_MINUTE_PASSED_INCREASE_BY = 0.05F;

}
