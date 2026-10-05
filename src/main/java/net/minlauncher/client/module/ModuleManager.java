package net.minlauncher.client.module;

import net.minlauncher.client.module.impl.hud.ArmorStatusModule;
import net.minlauncher.client.module.impl.hud.CPSModule;
import net.minlauncher.client.module.impl.hud.CoordsModule;
import net.minlauncher.client.module.impl.hud.DirectionModule;
import net.minlauncher.client.module.impl.hud.FPSModule;
import net.minlauncher.client.module.impl.hud.KeystrokesModule;
import net.minlauncher.client.module.impl.hud.PotionStatusModule;
import net.minlauncher.client.module.impl.hud.SpeedMeterModule;
import net.minlauncher.client.module.impl.pvp.KeepSprintModule;
import net.minlauncher.client.module.impl.pvp.ToggleSprintModule;
import net.minlauncher.client.module.impl.render.CustomModelsModule;
import net.minlauncher.client.module.impl.render.CustomTexturesModule;
import net.minlauncher.client.module.impl.render.NametagPreviewModule;
import net.minlauncher.client.module.impl.render.PrevisualizerModule;
import net.minlauncher.client.module.impl.render.ShadersModule;
import net.minlauncher.client.module.impl.survival.AutoToolModule;
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
        register(new SpeedMeterModule());
        register(new CoordsModule());
        register(new DirectionModule());
        register(new PotionStatusModule());

        // Módulos PvP
        register(new ToggleSprintModule());
        register(new KeepSprintModule());

        // Módulos Survival
        register(new FullbrightModule());
        register(new ZoomModule());
        register(new AutoToolModule());

        // Módulos Render / Integración Gráfica
        register(new ShadersModule());
        register(new CustomModelsModule());
        register(new CustomTexturesModule());
        register(new NametagPreviewModule());
        register(new PrevisualizerModule());
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

    /** Tecla presionada (PRESS): zoom especial o activación/toggle */
    public void onKeyDown(int key) {
        for (Module m : modules) {
            if (m.getKeyBind() == key && key != 0) {
                if (m instanceof ZoomModule zoom) {
                    zoom.startZoom();
                } else if (m.isHold()) {
                    m.setEnabled(true);
                } else {
                    m.toggle();
                }
            }
        }
    }

    /** Tecla soltada (RELEASE): finalización de zoom o módulos hold. */
    public void onKeyUp(int key) {
        for (Module m : modules) {
            if (m.getKeyBind() == key && key != 0) {
                if (m instanceof ZoomModule zoom) {
                    zoom.stopZoom();
                } else if (m.isHold()) {
                    m.setEnabled(false);
                }
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
