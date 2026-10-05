package net.minlauncher.client.module.impl.survival;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.NumberSetting;
import org.lwjgl.glfw.GLFW;

public class ZoomModule extends Module {
    private final NumberSetting zoomFactor = new NumberSetting("Nivel Zoom", "Multiplicador de aumento", 3.0, 1.5, 6.0, 0.5);
    private final BooleanSetting smoothCamera = new BooleanSetting("Cámara Cinemática", "Suaviza el movimiento al hacer zoom", true);

    public ZoomModule() {
        super("Zoom", "Aproximación de cámara para explorar el mapa estilo OptiFine", Category.SURVIVAL, GLFW.GLFW_KEY_C);
        addSetting(zoomFactor);
        addSetting(smoothCamera);
    }

    public NumberSetting getZoomFactor() {
        return zoomFactor;
    }

    public BooleanSetting getSmoothCamera() {
        return smoothCamera;
    }
}
