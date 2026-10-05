package net.minlauncher.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.cosmetics.Cosmetic;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ModeSetting;
import net.minlauncher.client.module.setting.NumberSetting;
import net.minlauncher.client.module.setting.Setting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;

    public ConfigManager() {
        File dir = new File(MinecraftClient.getInstance().runDirectory, "minlauncher");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.configFile = new File(dir, "config.json");
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            JsonObject root = new JsonObject();

            // Guardar Módulos
            JsonObject modulesObj = new JsonObject();
            for (Module m : MinLauncher.getInstance().getModuleManager().getModules()) {
                JsonObject mObj = new JsonObject();
                mObj.addProperty("enabled", m.isEnabled());
                mObj.addProperty("keybind", m.getKeyBind());

                if (m instanceof HudModule hm) {
                    mObj.addProperty("x", hm.getX());
                    mObj.addProperty("y", hm.getY());
                }

                JsonObject settingsObj = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    if (s instanceof BooleanSetting bs) {
                        settingsObj.addProperty(s.getName(), bs.getValue());
                    } else if (s instanceof NumberSetting ns) {
                        settingsObj.addProperty(s.getName(), ns.getValue());
                    } else if (s instanceof ModeSetting ms) {
                        settingsObj.addProperty(s.getName(), ms.getValue());
                    }
                }
                mObj.add("settings", settingsObj);
                modulesObj.add(m.getName(), mObj);
            }
            root.add("modules", modulesObj);

            // Guardar Cosméticos
            JsonObject cosObj = new JsonObject();
            for (Cosmetic cos : MinLauncher.getInstance().getCosmeticsManager().getCosmetics()) {
                cosObj.addProperty(cos.getId(), cos.isEquipped());
            }
            root.add("cosmetics", cosObj);

            GSON.toJson(root, writer);
        } catch (IOException e) {
            MinLauncher.LOGGER.error("Error guardando la configuración del cliente: ", e);
        }
    }

    public void loadConfig() {
        if (!configFile.exists()) return;

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (root.has("modules")) {
                JsonObject modulesObj = root.getAsJsonObject("modules");
                for (Module m : MinLauncher.getInstance().getModuleManager().getModules()) {
                    if (modulesObj.has(m.getName())) {
                        JsonObject mObj = modulesObj.getAsJsonObject(m.getName());
                        if (mObj.has("enabled")) m.setEnabled(mObj.get("enabled").getAsBoolean());
                        if (mObj.has("keybind")) m.setKeyBind(mObj.get("keybind").getAsInt());

                        if (m instanceof HudModule hm) {
                            if (mObj.has("x")) hm.setX(mObj.get("x").getAsInt());
                            if (mObj.has("y")) hm.setY(mObj.get("y").getAsInt());
                        }

                        if (mObj.has("settings")) {
                            JsonObject settingsObj = mObj.getAsJsonObject("settings");
                            for (Setting<?> s : m.getSettings()) {
                                if (settingsObj.has(s.getName())) {
                                    if (s instanceof BooleanSetting bs) {
                                        bs.setValue(settingsObj.get(s.getName()).getAsBoolean());
                                    } else if (s instanceof NumberSetting ns) {
                                        ns.setValue(settingsObj.get(s.getName()).getAsDouble());
                                    } else if (s instanceof ModeSetting ms) {
                                        ms.setValue(settingsObj.get(s.getName()).getAsString());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (root.has("cosmetics")) {
                JsonObject cosObj = root.getAsJsonObject("cosmetics");
                for (Cosmetic cos : MinLauncher.getInstance().getCosmeticsManager().getCosmetics()) {
                    if (cosObj.has(cos.getId())) {
                        cos.setEquipped(cosObj.get(cos.getId()).getAsBoolean());
                    }
                }
            }
        } catch (Exception e) {
            MinLauncher.LOGGER.error("Error cargando la configuración del cliente: ", e);
        }
    }
}
