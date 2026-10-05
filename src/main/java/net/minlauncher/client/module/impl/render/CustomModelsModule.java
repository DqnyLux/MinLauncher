package net.minlauncher.client.module.impl.render;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;

/**
 * Módulo de integración de Modelos de Entidad Personalizados (EMF / Entity Model Features).
 */
public class CustomModelsModule extends Module {
    private final BooleanSetting customAnimations = new BooleanSetting("Animaciones Custom", "Permite animaciones detalladas en mobs y jugadores", true);
    private final BooleanSetting entityPhysics = new BooleanSetting("Física de Entidades", "Física secundaria en capas y accesorios", true);
    private final BooleanSetting culling = new BooleanSetting("Optimización Oclusión", "No renderiza modelos fuera de la vista de cámara", true);

    public CustomModelsModule() {
        super("Modelos EMF", "Soporte de modelos y animaciones personalizadas de mobs", Category.RENDER);
        setEnabled(true);
        addSetting(customAnimations);
        addSetting(entityPhysics);
        addSetting(culling);
    }
}
