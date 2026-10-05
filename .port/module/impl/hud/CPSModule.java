package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.DrawContext;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

import java.util.ArrayList;
import java.util.List;

public class CPSModule extends HudModule {
    private static final List<Long> leftClicks = new ArrayList<>();
    private static final List<Long> rightClicks = new ArrayList<>();
    private static boolean wasLeftDown = false;
    private static boolean wasRightDown = false;

    public CPSModule() {
        super("CPS Counter", "Muestra los clics por segundo en tiempo real", 10, 80, 50, 16);
        setEnabled(true);
    }

    public static void registerClick(boolean right) {
        long now = System.currentTimeMillis();
        if (right) {
            rightClicks.add(now);
        } else {
            leftClicks.add(now);
        }
    }

    @Override
    public void onTick() {
        if (mc.options == null) return;
        boolean isLeftDown = mc.options.attackKey.isPressed();
        boolean isRightDown = mc.options.useKey.isPressed();

        if (isLeftDown && !wasLeftDown) registerClick(false);
        if (isRightDown && !wasRightDown) registerClick(true);

        wasLeftDown = isLeftDown;
        wasRightDown = isRightDown;
    }

    @Override
    public void render(DrawContext context, float delta) {
        long now = System.currentTimeMillis();
        leftClicks.removeIf(t -> now - t > 1000);
        rightClicks.removeIf(t -> now - t > 1000);

        String text = leftClicks.size() + " | " + rightClicks.size() + " CPS";
        int textWidth = mc.textRenderer.getWidth(text);
        setWidth(textWidth + 8);
        setHeight(16);

        if (getBackground().getValue()) {
            RenderUtils.drawRect(context, getX(), getY(), getWidth(), getHeight(), 0x77000000);
        }

        context.drawText(mc.textRenderer, text, getX() + 4, getY() + 4, getTextColor().getEffectiveColor(), true);
    }
}
