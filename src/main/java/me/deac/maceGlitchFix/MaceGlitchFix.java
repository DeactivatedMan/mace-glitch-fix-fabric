package me.deac.maceGlitchFix;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaceGlitchFix implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("template");
    public static final String VERSION = /*$ mod_version*/ "0.1.0";
    public static final String MINECRAFT = /*$ minecraft*/ "26.2";

    public long ySubtraction;
    public long minFallDist;

    public boolean logCancels;

    public boolean cancelFall;
    public boolean groundAttack;


    @Override
    public void onInitialize() {
        ModConfig.load();

        ySubtraction = ModConfig.get().ySubtraction;
        minFallDist = ModConfig.get().minFallDist;

        logCancels = ModConfig.get().logCancels;

        cancelFall = ModConfig.get().cancelFall;
        groundAttack = ModConfig.get().groundAttack;

        boolean devMode = ModConfig.get().developerMode;

        LOGGER.info(
            "Started with config:\n - ySubtraction: {}\n - minFallDist: {}\n - cancel fall: {}\n - do ground attack: {}\n - logCancels: {}\n - dev mode: {}",
            ySubtraction, minFallDist, cancelFall, groundAttack, logCancels, devMode
        );
        if (!cancelFall && groundAttack) LOGGER.warn("do-ground-attack enabled but requires cancel-fall-dmg to be enabled to function.");

        //? if !release
        LOGGER.info("Hello Fabric world!");
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

    public static boolean shouldCancelAttack(ServerPlayer attacker) {
        if (!attacker.getMainHandItem().is(Items.MACE)) return false; // Not a mace

        try (ServerLevel world = attacker.level()) {
            BlockPos posBelow = attacker.blockPosition().below(1);
            boolean isBlockBelow = !world.getBlockState(posBelow).isAir();

            // If the block below isn't air AND ffell from above the minimum
            // Minimum check is to allow for use of mace without falling (ie on ground or just critting)
            return isBlockBelow && attacker.fallDistance > 5;
        } catch (Exception _) {return false;}
    }
}