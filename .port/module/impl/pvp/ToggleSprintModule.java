package net.minlauncher.client.module.impl.pvp;

import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import org.lwjgl.glfw.GLFW;

public class ToggleSprintModule extends Module {
    public ToggleSprintModule() {
        super("ToggleSprint", "Mantiene al jugador corriendo automáticamente", Category.PVP, GLFW.GLFW_KEY_V);
        setEnabled(true);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.options != null) {
            if (!mc.player.isSneaking() && !mc.player.horizontalCollision && mc.player.input.hasForwardMovement()) {
                mc.player.setSprinting(true);
            }
        }
    }
}
