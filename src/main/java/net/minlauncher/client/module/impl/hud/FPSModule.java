package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

public class FPSModule extends HudModule {
    public FPSModule() {
        super("FPS Display", "Muestra los cuadros por segundo actuales", 10, 100, 45, 14);
        setEnabled(true);
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        int fps = mc.getFps();
        String text = fps + " FPS";
        setWidth(mc.font.width(text) + 8);
        setHeight(14);

        if (getBackground().getValue()) {
            RenderUtils.drawRect(extractor, getX(), getY(), getWidth(), getHeight(), 0x77000000);
        }

        extractor.text(mc.font, text, getX() + 4, getY() + 3, getTextColor().getEffectiveColor(), true);
    }
}