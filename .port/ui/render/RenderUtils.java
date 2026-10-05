package net.minlauncher.client.ui.render;

import net.minecraft.client.gui.DrawContext;

public class RenderUtils {

    public static void drawRect(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        // Renderizado eficiente en DrawContext sin overhead de shaders externos
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + radius, y + height - radius, color);
        context.fill(x + width - radius, y + radius, x + width, y + height - radius, color);
    }

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int thickness, int color) {
        context.fill(x, y, x + width, y + thickness, color); // Top
        context.fill(x, y + height - thickness, x + width, y + height, color); // Bottom
        context.fill(x, y + thickness, x + thickness, y + height - thickness, color); // Left
        context.fill(x + width - thickness, y + thickness, x + width, y + height - thickness, color); // Right
    }
}
