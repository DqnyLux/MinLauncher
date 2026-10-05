package net.minlauncher.client.module;

import net.minlauncher.client.module.impl.hud.ArmorStatusModule;
import net.minlauncher.client.module.impl.hud.CPSModule;
import net.minlauncher.client.module.impl.hud.FPSModule;
import net.minlauncher.client.module.impl.hud.KeystrokesModule;
import net.minlauncher.client.module.impl.pvp.ToggleSprintModule;
import net.minlauncher.client.module.impl.survival.FullbrightModule;
import net.minlauncher.client.module.impl.survival.ZoomModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // Módulos HUD
        register(new KeystrokesModule());
        register(new CPSModule());
        register(new FPSModule());
        register(new ArmorStatusModule());

        // Módulos PvP
        register(new ToggleSprintModule());

        // Módulos Survival
        register(new FullbrightModule());
        register(new ZoomModule());
    }

    public void register(Module module) {
        this.modules.add(module);
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<HudModule> getHudModules() {
        return modules.stream()
                .filter(m -> m instanceof HudModule)
                .map(m -> (HudModule) m)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        for (Module m : modules) {
            if (m.getClass().equals(clazz)) {
                return (T) m;
            }
        }
        return null;
    }

    public Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public void onKey(int key) {
        for (Module m : modules) {
            if (m.getKeyBind() == key && key != 0) {
                m.toggle();
            }
        }
    }

    public void onTick() {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }
}
