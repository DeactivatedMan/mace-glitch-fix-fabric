package me.deac.maceGlitchFix;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaceGlitchFix implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("template");
    public static final String VERSION = /*$ mod_version*/ "0.1.0";
    public static final String MINECRAFT = /*$ minecraft*/ "26.2";

    @Override
    public void onInitialize() {
        ModConfig.load();
        ModConfig config = ModConfig.get();

        LOGGER.info(
            "Started with config:\n - ySubtraction: {}\n - minFallDist: {}\n - cancel fall: {}\n - do ground attack: {}\n - logCancels: {}",
            config.ySubtraction, config.minFallDist, config.cancelFall, config.groundAttack, config.logCancels
        );
        if (!config.cancelFall && config.groundAttack) LOGGER.warn("do-ground-attack enabled but requires cancel-fall-dmg to be enabled to function.");
    }

    /**
     * Adapts to the {@link Identifier} changes introduced in 1.21.
     */
    public static Identifier id(String namespace, String path) {
        //? if <1.21 {
        /*return new Identifier(namespace, path);
         *///?} else
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    /*public static boolean shouldCancelAttack(ServerPlayer attacker) {
        if (!attacker.getMainHandItem().is(Items.MACE)) return false; // Not a mace

        try (ServerLevel world = attacker.level()) {
            BlockPos posBelow = attacker.blockPosition().below(1);
            boolean isBlockBelow = !world.getBlockState(posBelow).isAir();

            // If the block below isn't air AND ffell from above the minimum
            // Minimum check is to allow for use of mace without falling (ie on ground or just critting)
            return isBlockBelow && attacker.fallDistance > 5;
        } catch (Exception e) {return false;}
    }*/

    public static boolean shouldCancelAttack(ServerPlayer attacker, Entity target) {
        if (!attacker.getMainHandItem().is(Items.MACE)) return false;
        ModConfig config = ModConfig.get();

        ServerLevel world = (ServerLevel) attacker.level();

        BlockPos posBelow = attacker.blockPosition().below(config.ySubtraction);
        BlockState state = world.getBlockState(posBelow);
        boolean isBlockBelow = !state.isAir();
        boolean shouldCancel = isBlockBelow && attacker.fallDistance > config.minFallDist;

        if (shouldCancel) {
            if (config.logCancels) LOGGER.info(
                    "Cancelled attack by {} (uuid {}) falling from {} / {} blocks",
                    attacker.getName().getString(), attacker.getStringUUID(), attacker.fallDistance, config.minFallDist
            );

            if (config.cancelFall || config.groundAttack) attacker.fallDistance = 0;

            ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(posBelow, state);
            attacker.connection.send(packet);

            if (config.groundAttack) attacker.attack(target);
        }

        return shouldCancel;
    }
}