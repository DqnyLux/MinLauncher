package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

/**
 * Brújula HUD: muestra la direccion cardinal hacia la que miras (N/S/E/W),
 * calculo puro sobre el yaw del jugador (0=Sur, 90=Oeste, 180=Norte, -90=Este).
 */
public class DirectionModule extends HudModule {

    private static final String[] FACINGS = {
        "Sur", "Suroeste", "Oeste", "Noroeste", "Norte", "Noreste", "Este", "Sureste"
    };

    public DirectionModule() {
        super("Direction", "Muestra la direccion cardinal (brújula)", 10, 160, 60, 14);
        setEnabled(true);
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        if (mc.player == null) return;
        float yaw = mc.player.getYRot() % 360f;
        if (yaw < 0) yaw += 360f;
        int index = ((int) Math.round(yaw / 45.0)) % 8;
        String text = "Facing: " + FACINGS[index];
        setWidth(mc.font.width(text) + 8);
        setHeight(14);
        if (getBackground().getValue()) {
            RenderUtils.drawRect(extractor, getX(), getY(), getWidth(), getHeight(), 0x77000000);
        }
        extractor.text(mc.font, text, getX() + 4, getY() + 3, getTextColor().getEffectiveColor(), true);
    }
}