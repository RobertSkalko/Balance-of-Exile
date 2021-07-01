package com.robertx22.anti_mob_cheese.main;

import com.robertx22.anti_mob_cheese.configs.CheeseConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            return AutoConfig.getConfigScreen(CheeseConfig.class, screen)
                .get();
        };
    }

    @Override
    public String getModId() {
        return "anti_mob_cheese";
    }
}
