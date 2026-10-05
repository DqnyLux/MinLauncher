package net.minlauncher.client.module.impl.survival;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.NumberSetting;
import org.lwjgl.glfw.GLFW;

/**
 * Módulo de Zoom tipo OptiFine / Lunar:
 * - Se activa y desactiva en el menú de mods (si está apagado, presionar la tecla no hace nada).
 * - Cuando está encendido en el cliente, al mantener presionada la tecla (por defecto C),
 *   se realiza una aproximación fluida con interpolación de FOV y movimiento suave de cámara.
 */
public class ZoomModule extends Module {
    private final NumberSetting zoomFactor = new NumberSetting("Nivel Zoom", "Multiplicador de aumento", 3.0, 1.5, 6.0, 0.5);
    private final BooleanSetting smoothCamera = new BooleanSetting("Cámara Cinemática", "Suaviza el movimiento al hacer zoom", true);

    private boolean isZooming = false;
    private float progress = 0.0f;

    public ZoomModule() {
        super("Zoom", "Aproximación cinematográfica suave al mantener la tecla (estilo OptiFine)", Category.SURVIVAL, GLFW.GLFW_KEY_C);
        setEnabled(true);
        addSetting(zoomFactor);
        addSetting(smoothCamera);
    }

    public void startZoom() {
        if (!isEnabled()) return;
        this.isZooming = true;
        if (smoothCamera.getValue() && mc.options != null) {
            mc.options.smoothCamera = true;
        }
    }

    public void stopZoom() {
        this.isZooming = false;
        if (mc.options != null) {
            mc.options.smoothCamera = false;
        }
    }

    public boolean isCurrentlyZooming() {
        return isEnabled() && isZooming;
    }

    public void updateProgress(float delta) {
        float target = isCurrentlyZooming() ? 1.0f : 0.0f;
        // Transición lerp suave
        float speed = 0.25f;
        progress += (target - progress) * speed;
        if (progress < 0.001f) progress = 0.0f;
        if (progress > 0.999f) progress = 1.0f;
    }

    public float getProgress() {
        return progress;
    }

    public NumberSetting getZoomFactor() {
        return zoomFactor;
    }

    public BooleanSetting getSmoothCamera() {
        return smoothCamera;
    }

    @Override
    public void onDisable() {
        stopZoom();
        progress = 0.0f;
    }
}
