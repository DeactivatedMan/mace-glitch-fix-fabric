package me.deac.maceGlitchFix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "maceglitchfix.json");
    private static ModConfig INSTANCE = new ModConfig();

    public double minFallDist = 5;
    public int ySubtraction = 1;
    public boolean cancelFall = false;
    public boolean groundAttack = false;
    public boolean logCancels = true;

    public static ModConfig get() {return INSTANCE;}
    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            INSTANCE = gson.fromJson(reader, ModConfig.class);
            if (INSTANCE == null) {INSTANCE = new ModConfig();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
