package com.fabforreal.keepinventorypenalty.config;

import com.fabforreal.keepinventorypenalty.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    public static String file = "keepInventoryPenaltyConfig.json";
    private static ConfigModel CONFIG = new ConfigModel();
    private static Path getSaveFolder(MinecraftServer server) {
       return server.getWorldPath(LevelResource.ROOT);
    }
    private static Path getSaveFile(MinecraftServer server)
    {
        return getSaveFolder(server).resolve(file);
    }

    public static ConfigModel GetConfig() {
        if(CONFIG == null) {
            CONFIG = new ConfigModel();
        }
        return CONFIG;
    }

    public static void LoadConfig(MinecraftServer server) {
        try {
            Path path = getSaveFile(server);

            if(Files.exists(path)) {
                String file = getFile(server);

                Gson gson = new Gson();
                ConfigModel data = gson.fromJson(file, ConfigModel.class);
                if(data == null) {
                    CONFIG = new ConfigModel();
                    return;
                }

                CONFIG = data;

                Constants.LOG.info("SUCCESSFULLY LOADED CONFIG");
            }
            else {
                SaveConfig(server, new ConfigModel());
            }
        }
        catch (Exception e) {
            Constants.LOG.error("ERROR LOADING CONFIG; " + e.getMessage());
        }
    }

    public static void SaveConfig(MinecraftServer server, ConfigModel data) {
        try {
            if(data == null)
                data = new ConfigModel();

            BufferedWriter writer = new BufferedWriter(new FileWriter(getSaveFile(server).toString()));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String saveString = gson.toJson(data);

            writer.write(saveString);
            writer.close();
            CONFIG = data;

            Constants.LOG.info("SUCCESSFULLY SAVED CONFIG");
        }
        catch (Exception e) {
            Constants.LOG.error("ERROR SAVING CONFIG; " + e.getMessage());
        }
    }

    private static String getFile(MinecraftServer server) {
        try {
            Path path = getSaveFile(server);

            if (!Files.exists(path))
                return null;
            return Files.readString(path);
        }
        catch (Exception e) {
            Constants.LOG.error(e.getMessage());
            return null;
        }
    }
}