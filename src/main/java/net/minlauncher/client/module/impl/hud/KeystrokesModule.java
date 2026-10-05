package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.ui.render.RenderUtils;

public class KeystrokesModule extends HudModule {
    private final BooleanSetting showMouse = new BooleanSetting("Mostrar Ratón", "Muestra botones LMB y RMB", true);
    private final BooleanSetting showSpace = new BooleanSetting("Mostrar Espacio", "Muestra barra de salto", true);

    public KeystrokesModule() {
        super("Keystrokes", "Muestra las teclas presionadas en pantalla", 10, 10, 60, 60);
        addSetting(showMouse);
        addSetting(showSpace);
        setEnabled(true);
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        if (mc.options == null) return;

        int curX = getX();
        int curY = getY();
        int size = 18;
        int gap = 2;

        // W
        drawKey(extractor, curX + size + gap, curY, size, size, "W", mc.options.keyUp.isDown());
        // A, S, D
        drawKey(extractor, curX, curY + size + gap, size, size, "A", mc.options.keyLeft.isDown());
        drawKey(extractor, curX + size + gap, curY + size + gap, size, size, "S", mc.options.keyDown.isDown());
        drawKey(extractor, curX + (size + gap) * 2, curY + size + gap, size, size, "D", mc.options.keyRight.isDown());

        int nextY = curY + (size + gap) * 2;

        // Botones de ratón
        if (showMouse.getValue()) {
            int mouseWidth = (size * 3 + gap * 2 - gap) / 2;
            drawKey(extractor, curX, nextY, mouseWidth, size, "LMB", mc.options.keyAttack.isDown());
            drawKey(extractor, curX + mouseWidth + gap, nextY, mouseWidth, size, "RMB", mc.options.keyUse.isDown());
            nextY += size + gap;
        }

        // Barra de espacio
        if (showSpace.getValue()) {
            int spaceWidth = size * 3 + gap * 2;
            drawKey(extractor, curX, nextY, spaceWidth, 10, "—", mc.options.keyJump.isDown());
            nextY += 10 + gap;
        }

        setHeight(nextY - curY);
        setWidth(size * 3 + gap * 2);
    }

    private void drawKey(GuiGraphicsExtractor extractor, int x, int y, int w, int h, String text, boolean pressed) {
        int bgColor = pressed ? 0x88FFFFFF : 0x88000000;
        int txtColor = pressed ? 0xFF000000 : getTextColor().getEffectiveColor();

        RenderUtils.drawRect(extractor, x, y, w, h, bgColor);
        int textWidth = mc.font.width(text);
        extractor.text(mc.font, text, x + (w - textWidth) / 2, y + (h - 8) / 2, txtColor, false);
    }
}