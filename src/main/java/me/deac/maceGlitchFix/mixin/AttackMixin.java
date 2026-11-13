package me.deac.maceGlitchFix.mixin;

import me.deac.maceGlitchFix.MaceGlitchFix;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class AttackMixin {

    @Inject(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void maceGlitchFix$onAttack(Entity target, CallbackInfo ci) {
        Player player = (Player) (Object) this;

        // Check if this player instance is actually a ServerPlayer safely
        if (!(player instanceof ServerPlayer attacker)) return;
        if (MaceGlitchFix.shouldCancelAttack(attacker, target)) ci.cancel();
    }
}