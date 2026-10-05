package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
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
    public void render(DrawContext context, float delta) {
        if (mc.options == null) return;

        int curX = getX();
        int curY = getY();
        int size = 18;
        int gap = 2;

        // W
        drawKey(context, curX + size + gap, curY, size, size, "W", mc.options.forwardKey.isPressed());
        // A, S, D
        drawKey(context, curX, curY + size + gap, size, size, "A", mc.options.leftKey.isPressed());
        drawKey(context, curX + size + gap, curY + size + gap, size, size, "S", mc.options.backKey.isPressed());
        drawKey(context, curX + (size + gap) * 2, curY + size + gap, size, size, "D", mc.options.rightKey.isPressed());

        int nextY = curY + (size + gap) * 2;

        // Mouse buttons
        if (showMouse.getValue()) {
            int mouseWidth = (size * 3 + gap * 2 - gap) / 2;
            drawKey(context, curX, nextY, mouseWidth, size, "LMB", mc.options.attackKey.isPressed());
            drawKey(context, curX + mouseWidth + gap, nextY, mouseWidth, size, "RMB", mc.options.useKey.isPressed());
            nextY += size + gap;
        }

        // Space bar
        if (showSpace.getValue()) {
            int spaceWidth = size * 3 + gap * 2;
            drawKey(context, curX, nextY, spaceWidth, 10, "―", mc.options.jumpKey.isPressed());
            nextY += 10 + gap;
        }

        setHeight(nextY - curY);
        setWidth(size * 3 + gap * 2);
    }

    private void drawKey(DrawContext context, int x, int y, int w, int h, String text, boolean pressed) {
        int bgColor = pressed ? 0x88FFFFFF : 0x88000000;
        int txtColor = pressed ? 0xFF000000 : getTextColor().getEffectiveColor();

        RenderUtils.drawRect(context, x, y, w, h, bgColor);
        int textWidth = mc.textRenderer.getWidth(text);
        context.drawText(mc.textRenderer, text, x + (w - textWidth) / 2, y + (h - 8) / 2, txtColor, false);
    }
}
