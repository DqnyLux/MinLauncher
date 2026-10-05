package net.minlauncher.client.module.impl.survival;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.NumberSetting;
import org.lwjgl.glfw.GLFW;

public class FullbrightModule extends Module {
    private final NumberSetting gamma = new NumberSetting("Brillo", "Nivel de luminosidad en cuevas y noche", 15.0, 1.0, 20.0, 1.0);

    public FullbrightModule() {
        super("Fullbright", "Iluminación nocturna y cuevas claras para supervivencia", Category.SURVIVAL, GLFW.GLFW_KEY_B);
        addSetting(gamma);
        setEnabled(true);
    }
}
