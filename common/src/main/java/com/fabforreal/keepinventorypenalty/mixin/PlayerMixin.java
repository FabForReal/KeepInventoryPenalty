package com.fabforreal.keepinventorypenalty.mixin;

import com.fabforreal.keepinventorypenalty.config.ConfigManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow protected abstract void destroyVanishingCursedItems();

    @Inject(at = @At("TAIL"), method = "dropEquipment")
    private void Foo(CallbackInfo ci) {
        var player = (Player) (Object) this;
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            if(ConfigManager.GetConfig().removeCurseOfVanishingItems) {
                destroyVanishingCursedItems();
            }
        }
    }
}