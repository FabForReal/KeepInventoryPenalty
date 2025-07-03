package com.fabforreal.keepinventorypenalty;

import com.fabforreal.keepinventorypenalty.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PenaltyManager {
    public static void SendLocalDeathMessage(ServerPlayer instance, int totalLoss) {
        Component text = Component.literal("You died and lost ")
                .append(Component.literal(Integer.toString(totalLoss)).withStyle(ChatFormatting.RED))
                .append(Component.literal(" experience levels"));

        instance.displayClientMessage(text, false);
    }


    public static void SendShameMessage(ServerPlayer instance, int totalLoss) {
        String playerName = instance.getDisplayName().getString();
        Component text = Component.literal(playerName)
                .append(Component.literal(" died and lost ")
                        .append(Component.literal(Integer.toString(totalLoss)).withStyle(ChatFormatting.RED))
                        .append(Component.literal(" experience levels")));

        Broadcast(instance, text);
    }


    public static void SendAttackShameMessage(ServerPlayer instance, ServerPlayer attacker, int totalLoss) {
        String playerName = instance.getDisplayName().getString();
        String attackerPlayerName = attacker.getDisplayName().getString();
        Component text = Component.literal(attackerPlayerName)
                .append(Component.literal(" killed ")
                        .append(Component.literal(playerName)
                                .append(Component.literal(" and stole "))
                                .append(Component.literal(Integer.toString(totalLoss)).withStyle(ChatFormatting.RED))
                                .append(Component.literal(" experience levels"))));

        Broadcast(instance, text);
    }

    public static void GiveAttackerXP(ServerPlayer instance, ServerPlayer attacker, int totalLoss) {
        attacker.setExperienceLevels(attacker.experienceLevel += totalLoss);

        // shame message if enabled
        if(ConfigManager.GetConfig().globalPenaltyChatMessage)
            SendAttackShameMessage(instance, attacker, totalLoss);
    }

    private static void Broadcast(ServerPlayer instance, Component text) {
        for (Player player : instance.getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(text);
        }
    }
}