package net.minlauncher.client.module.impl.render;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ModeSetting;
import net.minlauncher.client.module.setting.NumberSetting;

/**
 * Módulo de integración de Shaders (compatible con el ecosistema Iris/Oculus).
 * Permite alternar shaders rápidamente y ajustar la calidad del pipeline.
 */
public class ShadersModule extends Module {
    private final ModeSetting profile = new ModeSetting("Perfil Calidad", "Nivel de optimización gráfica", "Medio", new String[]{"Rendimiento", "Medio", "Ultra", "Cinemático"});
    private final BooleanSetting shadowMap = new BooleanSetting("Sombras Dinámicas", "Activa el renderizado de sombras en tiempo real", true);
    private final NumberSetting renderScale = new NumberSetting("Escala de Render", "Multiplicador de resolución interna", 1.0, 0.5, 2.0, 0.25);
    private final BooleanSetting handDepth = new BooleanSetting("Profundidad de Mano", "Renderiza la mano en primer plano sin clipping", true);

    public ShadersModule() {
        super("Shaders (Iris)", "Pipeline gráfico avanzado y paquetes de sombras dinámicas", Category.RENDER);
        setEnabled(true);
        addSetting(profile);
        addSetting(shadowMap);
        addSetting(renderScale);
        addSetting(handDepth);
    }
}
