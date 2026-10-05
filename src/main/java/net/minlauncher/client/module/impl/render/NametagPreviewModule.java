package net.minlauncher.client.module.impl.render;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ColorSetting;

/**
 * Módulo NametagPreview: Personaliza y previsualiza las etiquetas de nombre de los jugadores (HP, armadura, insignia).
 */
public class NametagPreviewModule extends Module {
    private final BooleanSetting showHp = new BooleanSetting("Mostrar HP", "Muestra la barra o números de vida en el nametag", true);
    private final BooleanSetting showBadges = new BooleanSetting("Mostrar Insignias", "Muestra las insignias de MinLauncher junto al nombre", true);
    private final BooleanSetting bgTransparent = new BooleanSetting("Fondo Translúcido", "Fondo oscuro semitransparente detrás del nombre", true);

    public NametagPreviewModule() {
        super("NametagPreview", "Personaliza nombres con vida, insignias y renderizado limpio", Category.RENDER);
        setEnabled(true);
        addSetting(showHp);
        addSetting(showBadges);
        addSetting(bgTransparent);
    }
}
