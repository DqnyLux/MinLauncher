package net.minlauncher.client.module;

import net.minecraft.client.Minecraft;
import net.minlauncher.client.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private int keyBind;
    private boolean enabled;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean hold; // true = se activa manteniendo pulsada la tecla

    public Module(String name, String description, Category category, int defaultKeyBind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBind = defaultKeyBind;
        this.enabled = false;
        this.hold = false;
    }

    public Module(String name, String description, Category category) {
        this(name, description, category, 0);
    }

    /** Marca el módulo para activarse SOLO mientras se mantiene pulsada su tecla (estilo Zoom/OptiFine). */
    protected void setHold(boolean hold) {
        this.hold = hold;
    }

    public boolean isHold() {
        return hold;
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (this.enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public void addSetting(Setting<?> setting) {
        this.settings.add(setting);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }
}
