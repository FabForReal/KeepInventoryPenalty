package com.fabforreal.keepinventorypenalty.mixin;

import com.fabforreal.keepinventorypenalty.PenaltyCalculator;
import com.fabforreal.keepinventorypenalty.PenaltyManager;
import com.fabforreal.keepinventorypenalty.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(at = @At("TAIL"), method = "die")
    private void Death(DamageSource cause, CallbackInfo ci) {
        ServerPlayer inst = (ServerPlayer) (Object) this;

        if(!inst.getServer().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        if(!ConfigManager.GetConfig().penaltyEnabled) return;

        Entity attacker = cause.getEntity();

        float loss;
        // random or set penalty amount depending on config
        if(ConfigManager.GetConfig().randomPenaltyEnabled) {
            loss = PenaltyCalculator.RandomizedPenalty(inst.experienceLevel);
        }
        else {
            loss = PenaltyCalculator.DefaultPenalty(inst.experienceLevel);
        }

        int roundedLoss = Math.round(loss);
        int totalLoss = inst.experienceLevel - roundedLoss;

        if(attacker instanceof ServerPlayer) {
            if(ConfigManager.GetConfig().giveExperienceToAttackingPlayer) {
                // give the attacker the xp
                PenaltyManager.GiveAttackerXP(inst, (ServerPlayer) attacker, totalLoss);
            }
            else {
                // regular xp penalty. just send a shame message if enabled
                if(ConfigManager.GetConfig().globalPenaltyChatMessage)
                    PenaltyManager.SendShameMessage(inst, totalLoss);
            }
        }
        else {
            // Default. just send a chat message if enabled
            if(ConfigManager.GetConfig().globalPenaltyChatMessage) {
                PenaltyManager.SendShameMessage(inst, totalLoss);
            }
        }

        inst.setExperienceLevels(roundedLoss);

        // if global chat message is disabled, send a local message informing the player
        if(!ConfigManager.GetConfig().globalPenaltyChatMessage) {
            PenaltyManager.SendLocalDeathMessage(inst, totalLoss);
        }
    }
}