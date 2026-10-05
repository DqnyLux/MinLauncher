package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

/** Velocimetro (m/s): mide el desplazamiento horizontal real del jugador por tick. */
public class SpeedMeterModule extends HudModule {
    private double lastX, lastZ;
    private double speed; // bloques/segundo
    private boolean init;

    public SpeedMeterModule() {
        super("SpeedMeter", "Muestra tu velocidad en bloques por segundo", 10, 120, 100, 14);
        setEnabled(true);
    }

    @Override
    public void onTick() {
        if (mc.player == null) { init = false; return; }
        double x = mc.player.getX(), z = mc.player.getZ();
        if (!init) { lastX = x; lastZ = z; init = true; return; }
        double dx = x - lastX, dz = z - lastZ;
        speed = Math.hypot(dx, dz) * 20.0; // 20 ticks/segundo
        lastX = x; lastZ = z;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        if (mc.player == null) return;
        String text = String.format("Speed: %.1f m/s", speed);
        setWidth(mc.font.width(text) + 8);
        setHeight(14);
        if (getBackground().getValue()) {
            RenderUtils.drawRect(extractor, getX(), getY(), getWidth(), getHeight(), 0x77000000);
        }
        extractor.text(mc.font, text, getX() + 4, getY() + 3, getTextColor().getEffectiveColor(), true);
    }
}