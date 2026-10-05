package net.minlauncher.client.module.impl.render;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;

/**
 * Módulo de integración de Texturas de Entidad (ETF / Entity Texture Features).
 */
public class CustomTexturesModule extends Module {
    private final BooleanSetting emissiveTextures = new BooleanSetting("Texturas Emisivas", "Ojos brillantes y texturas luminosas en la oscuridad", true);
    private final BooleanSetting randomMobs = new BooleanSetting("Variantes de Mobs", "Variaciones aleatorias de texturas según el bioma", true);
    private final BooleanSetting connectedTextures = new BooleanSetting("Texturas Conectadas", "Conecta bloques adyacentes de cristal y minerales", true);

    public CustomTexturesModule() {
        super("Texturas ETF", "Texturas emisivas y variaciones dinámicas de entidades", Category.RENDER);
        setEnabled(true);
        addSetting(emissiveTextures);
        addSetting(randomMobs);
        addSetting(connectedTextures);
    }
}
