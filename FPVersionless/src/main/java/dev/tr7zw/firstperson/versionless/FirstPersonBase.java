package dev.tr7zw.firstperson.versionless;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.tr7zw.firstperson.versionless.config.ConfigUpgrader;
import dev.tr7zw.firstperson.versionless.config.FirstPersonSettings;
import lombok.Getter;
import lombok.Setter;

public class FirstPersonBase {

    public static final Logger LOGGER = LogManager.getLogger("FirstPersonModel");
    @Getter
    @Setter
    private boolean isRenderingPlayer = false;
    @Getter
    @Setter
    private boolean enabled = true;
    @Getter
    private FirstPersonSettings config = null;
    private File settingsFile = new File("config", "firstperson.json");

    public void loadConfig() {
        if (settingsFile.exists()) {
            try {
                config = new Gson().fromJson(
                        new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8),
                        FirstPersonSettings.class);
            } catch (Exception ex) {
            	LOGGER.warn("Unable to load the config, creating a new one.", ex);
            }
        }
        if (config == null) {
            config = new FirstPersonSettings();
        }
        ConfigUpgrader.upgradeConfig(config);
        writeSettings();
        enabled = config.enabledByDefault;
    }

    public void writeSettings() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(config);
        try {
            Files.write(settingsFile.toPath(), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
        	LOGGER.warn("Error while saving the config!", e);
        }
    }

    public void resetSettings() {
        config = new FirstPersonSettings();
    }

    /**
     * Checks if a class exists or not
     * 
     * @param name
     * @return
     */
    protected static boolean isValidClass(String name) {
        try {
            if (Class.forName(name) != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            // ignored
        }
        return false;
    }

}
