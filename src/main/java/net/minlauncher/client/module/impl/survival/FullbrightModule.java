package net.minlauncher.client.module.impl.survival;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.NumberSetting;
import org.lwjgl.glfw.GLFW;

public class FullbrightModule extends Module {
    private final NumberSetting gamma = new NumberSetting("Brillo", "Nivel de luminosidad en cuevas y noche", 1.0, 0.5, 1.0, 0.1);
    private Double originalGamma;

    public FullbrightModule() {
        super("Fullbright", "Iluminación nocturna y cuevas claras para supervivencia", Category.SURVIVAL, GLFW.GLFW_KEY_B);
        addSetting(gamma);
        setEnabled(true);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            originalGamma = mc.options.gamma().get();
            mc.options.gamma().set(gamma.getValue());
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null && originalGamma != null) {
            mc.options.gamma().set(originalGamma);
        }
    }
}