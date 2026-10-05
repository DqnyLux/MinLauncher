package net.minlauncher.client;

import net.fabricmc.api.ClientModInitializer;
import net.minlauncher.client.config.ConfigManager;
import net.minlauncher.client.cosmetics.CosmeticsManager;
import net.minlauncher.client.cosmetics.badge.BadgeManager;
import net.minlauncher.client.module.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinLauncher implements ClientModInitializer {
    public static final String CLIENT_NAME = "MinLauncher";
    public static final String CLIENT_VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(CLIENT_NAME);

    private static MinLauncher instance;

    private final ModuleManager moduleManager = new ModuleManager();
    private final CosmeticsManager cosmeticsManager = new CosmeticsManager();
    private final BadgeManager badgeManager = new BadgeManager();
    private final ConfigManager configManager = new ConfigManager();

    public static MinLauncher getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;

        moduleManager.init();
        cosmeticsManager.init();
        badgeManager.init();

        // Cargar configuración persistidaf (.msinecraft/minlauncher/config.json)
        configManager.loadConfig();

        // Guardar en salida
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                try {
                    configManager.saveConfig();
                } catch (Exception e) {
                    LOGGER.error("Error guardando la configuración en salida", e);
                }
            }
        }, "MinLauncherConfigSave"));

        LOGGER.info("{} v{} inicializado (Minecraft 26.1.2, Java 25).",
                CLIENT_NAME, CLIENT_VERSION);
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CosmeticsManager getCosmeticsManager() {
        return cosmeticsManager;
    }

    public BadgeManager getBadgeManager() {
        return badgeManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}