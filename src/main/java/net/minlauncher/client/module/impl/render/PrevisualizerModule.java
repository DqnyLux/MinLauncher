package net.minlauncher.client.module.impl.render;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ModeSetting;

/**
 * Módulo Previsualizer (Block & Item Previsualizer): Muestra información detallada al mirar bloques y entidades.
 */
public class PrevisualizerModule extends Module {
    private final ModeSetting mode = new ModeSetting("Modo Inspector", "Información detallada a mostrar", "Completo", new String[]{"Compacto", "Completo", "Minimalista"});
    private final BooleanSetting showCoordinates = new BooleanSetting("Ver Coordenadas", "Muestra coordenadas del bloque apuntado", true);
    private final BooleanSetting showDurability = new BooleanSetting("Durabilidad de Ítems", "Muestra la vida útil restante de herramientas y armadura", true);

    public PrevisualizerModule() {
        super("Previsualizer", "Previsualización avanzada de bloques, entidades e ítems en pantalla", Category.RENDER);
        setEnabled(true);
        addSetting(mode);
        addSetting(showCoordinates);
        addSetting(showDurability);
    }
}
