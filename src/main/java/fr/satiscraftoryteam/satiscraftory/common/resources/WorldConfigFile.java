package fr.satiscraftoryteam.satiscraftory.common.resources;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class WorldConfigFile {
    private WorldConfigFile() {
    }

    public static CommentedFileConfig openToml(MinecraftServer server, String modId, String fileName) {
        Path configPath = server.getWorldPath(LevelResource.ROOT)
                .resolve("config")
                .resolve(modId)
                .resolve(fileName);

        try {
            Files.createDirectories(configPath.getParent());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to create config directory for " + configPath, exception);
        }

        CommentedFileConfig config = CommentedFileConfig.builder(configPath, TomlFormat.instance()).sync().build();
        config.load();
        return config;
    }

    public static void close(CommentedFileConfig config) {
        if (config != null) {
            config.close();
        }
    }
}
