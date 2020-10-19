package com.robertx22.balance_of_exile.configs;

import com.robertx22.balance_of_exile.main.ModAction;

public class AffectsWhatConfig {

    private boolean AFFECT_VANILLA_LOOT_TABLES = false;
    private boolean AFFECT_AGE_OF_EXILE_LOOT = true;
    private boolean AFFECT_AGE_OF_EXILE_EXP = true;

    public boolean affects(ModAction action) {

        if (action == ModAction.AOE_EXP) {
            return AFFECT_AGE_OF_EXILE_EXP;
        }
        if (action == ModAction.AOE_LOOT) {
            return AFFECT_AGE_OF_EXILE_LOOT;
        }
        if (action == ModAction.VANILLA_LOOT) {
            return AFFECT_VANILLA_LOOT_TABLES;
        }

        System.out.print("No enum: " + action.name());

        return false;
    }

}
