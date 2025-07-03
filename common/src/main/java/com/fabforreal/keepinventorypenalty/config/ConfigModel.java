package com.fabforreal.keepinventorypenalty.config;

public class ConfigModel {
    public boolean penaltyEnabled = true;
    public float penaltyPercentage = 0.5f;
    public boolean globalPenaltyChatMessage = false;

    public boolean randomPenaltyEnabled = false;
    public float minRandomPenalty = 0.0f;
    public float maxRandomPenalty = 1.0f;

    public boolean removeCurseOfVanishingItems = true;

    public boolean giveExperienceToAttackingPlayer = false;
}
