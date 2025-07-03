package com.fabforreal.keepinventorypenalty;

import com.fabforreal.keepinventorypenalty.config.ConfigManager;
import net.minecraft.util.RandomSource;

public class PenaltyCalculator {
    public static float DefaultPenalty(int experienceLevel)
    {
        float loss = experienceLevel * ConfigManager.GetConfig().penaltyPercentage;
        return experienceLevel - loss;
    }

    public static float RandomizedPenalty(int experienceLevel)
    {
        float min = ConfigManager.GetConfig().minRandomPenalty;
        float max = ConfigManager.GetConfig().maxRandomPenalty;
        float rand = min + RandomSource.create().nextFloat() * (max - min);

        float loss = experienceLevel * rand;
        return experienceLevel - loss;
    }
}
