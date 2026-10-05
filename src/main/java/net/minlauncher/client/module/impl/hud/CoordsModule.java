package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

/** Coordenadas: muestra X/Y/Z del jugador en el HUD (QOL legal de info). */
public class CoordsModule extends HudModule {
    public CoordsModule() {
        super("Coords", "Muestra tus coordenadas X/Y/Z", 10, 140, 70, 14);
        setEnabled(true);
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        if (mc.player == null) return;
        int x = (int) mc.player.getX();
        int y = mc.player.getBlockY();
        int z = (int) mc.player.getZ();
        String text = "XYZ " + x + " " + y + " " + z;
        setWidth(mc.font.width(text) + 8);
        setHeight(14);
        if (getBackground().getValue()) {
            RenderUtils.drawRect(extractor, getX(), getY(), getWidth(), getHeight(), 0x77000000);
        }
        extractor.text(mc.font, text, getX() + 4, getY() + 3, getTextColor().getEffectiveColor(), true);
    }
}