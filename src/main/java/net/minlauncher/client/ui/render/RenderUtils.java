package net.minlauncher.client.ui.render;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Motor de renderizado visual moderno de MinLauncher.
 * Soporta rectángulos con esquinas redondeadas reales, gradientes verticales y
 * horizontales con bordes redondeados, resplandores suaves (glow), sombras difusas
 * y tipografía estilizada con sombra limpia.
 */
public final class RenderUtils {

    private RenderUtils() {}

    /** Rectángulo plano simple. */
    public static void drawRect(GuiGraphicsExtractor ex, int x, int y, int w, int h, int color) {
        ex.fill(x, y, x + w, y + h, color);
    }

    /** Borde rectangular simple de 1 o más píxeles. */
    public static void drawBorder(GuiGraphicsExtractor ex, int x, int y, int w, int h, int thickness, int color) {
        ex.fill(x, y, x + w, y + thickness, color);                      // top
        ex.fill(x, y + h - thickness, x + w, y + h, color);              // bottom
        ex.fill(x, y + thickness, x + thickness, y + h - thickness, color); // left
        ex.fill(x + w - thickness, y + thickness, x + w, y + h - thickness, color); // right
    }

    /** Gradiente vertical. */
    public static void drawGradientV(GuiGraphicsExtractor ex, int x, int y, int w, int h, int colorTop, int colorBottom) {
        ex.fillGradient(x, y, x + w, y + h, colorTop, colorBottom);
    }

    /** Gradiente horizontal. */
    public static void drawGradientH(GuiGraphicsExtractor ex, int x, int y, int w, int h, int colorLeft, int colorRight) {
        for (int col = 0; col < w; col++) {
            float t = (float) col / (float) Math.max(1, w - 1);
            int color = interpolateColor(colorLeft, colorRight, t);
            ex.fill(x + col, y, x + col + 1, y + h, color);
        }
    }

    /**
     * Dibuja un rectángulo con esquinas redondeadas reales mediante cálculo de arco.
     */
    public static void drawRoundedRect(GuiGraphicsExtractor ex, int x, int y, int w, int h, int radius, int color) {
        if (radius <= 0) {
            ex.fill(x, y, x + w, y + h, color);
            return;
        }
        int r = Math.min(radius, Math.min(w, h) / 2);

        // Filas superiores curvadas
        for (int i = 0; i < r; i++) {
            int dx = r - (int) Math.round(Math.sqrt((double) r * r - (double) (r - 1 - i) * (r - 1 - i)));
            ex.fill(x + dx, y + i, x + w - dx, y + i + 1, color);
        }

        // Cuerpo central recto
        if (h > 2 * r) {
            ex.fill(x, y + r, x + w, y + h - r, color);
        }

        // Filas inferiores curvadas
        for (int i = 0; i < r; i++) {
            int dy = i;
            int dx = r - (int) Math.round(Math.sqrt((double) r * r - (double) dy * dy));
            ex.fill(x + dx, y + h - r + i, x + w - dx, y + h - r + i + 1, color);
        }
    }

    /**
     * Dibuja un rectángulo con esquinas redondeadas y relleno de gradiente vertical suave.
     */
    public static void drawRoundedGradientV(GuiGraphicsExtractor ex, int x, int y, int w, int h, int radius, int colorTop, int colorBottom) {
        if (radius <= 0) {
            ex.fillGradient(x, y, x + w, y + h, colorTop, colorBottom);
            return;
        }
        int r = Math.min(radius, Math.min(w, h) / 2);

        for (int row = 0; row < h; row++) {
            float t = (float) row / (float) Math.max(1, h - 1);
            int color = interpolateColor(colorTop, colorBottom, t);
            int dx = 0;
            if (row < r) {
                dx = r - (int) Math.round(Math.sqrt((double) r * r - (double) (r - 1 - row) * (r - 1 - row)));
            } else if (row >= h - r) {
                int dy = row - (h - r);
                dx = r - (int) Math.round(Math.sqrt((double) r * r - (double) dy * dy));
            }
            ex.fill(x + dx, y + row, x + w - dx, y + row + 1, color);
        }
    }

    /**
     * Dibuja el contorno (borde) redondeado siguiendo fielmente la curvatura del radio.
     */
    public static void drawRoundedOutline(GuiGraphicsExtractor ex, int x, int y, int w, int h, int radius, int thickness, int color) {
        if (radius <= 0) {
            drawBorder(ex, x, y, w, h, thickness, color);
            return;
        }
        int r = Math.min(radius, Math.min(w, h) / 2);
        int t = Math.max(1, thickness);

        for (int row = 0; row < h; row++) {
            int outerDx = 0;
            if (row < r) {
                outerDx = r - (int) Math.round(Math.sqrt((double) r * r - (double) (r - 1 - row) * (r - 1 - row)));
            } else if (row >= h - r) {
                int dy = row - (h - r);
                outerDx = r - (int) Math.round(Math.sqrt((double) r * r - (double) dy * dy));
            }

            if (row < t || row >= h - t) {
                // Fila superior o inferior completa
                ex.fill(x + outerDx, y + row, x + w - outerDx, y + row + 1, color);
            } else {
                // Bordes laterales con grosor t
                ex.fill(x + outerDx, y + row, x + outerDx + t, y + row + 1, color);
                ex.fill(x + w - outerDx - t, y + row, x + w - outerDx, y + row + 1, color);
            }
        }
    }

    /**
     * Dibuja un resplandor suave (glow) difuso alrededor del elemento.
     */
    public static void drawGlow(GuiGraphicsExtractor ex, int x, int y, int w, int h, int radius, int layers, int glowColor) {
        int alpha = (glowColor >> 24) & 0xFF;
        int rgb = glowColor & 0xFFFFFF;
        for (int i = layers; i >= 1; i--) {
            int layerAlpha = (int) ((float) alpha * (1.0f - (float) i / (layers + 1)) * 0.45f);
            if (layerAlpha <= 0) continue;
            int c = (layerAlpha << 24) | rgb;
            drawRoundedRect(ex, x - i, y - i, w + i * 2, h + i * 2, radius + i, c);
        }
    }

    /**
     * Sombra caída difusa simulada por capas.
     */
    public static void drawShadow(GuiGraphicsExtractor ex, int x, int y, int w, int h, int depth, int shadowColor) {
        int a = (shadowColor >> 24) & 0xFF;
        int rgb = shadowColor & 0xFFFFFF;
        for (int i = depth; i >= 1; i--) {
            int layerAlpha = (int) ((float) a * ((float) (depth - i + 1) / (float) (depth * 1.6f)));
            if (layerAlpha <= 0) continue;
            int c = (layerAlpha << 24) | rgb;
            drawRoundedRect(ex, x - i, y + 2, w + i * 2, h + i, 4 + i, c);
        }
    }

    /**
     * Dibuja una píldora moderna / badge con texto centrado y acento.
     */
    public static void drawPill(GuiGraphicsExtractor ex, Font font, String text, int x, int y, int bg, int border, int textColor) {
        int tw = font.width(text);
        int padH = 6, padV = 3;
        int w = tw + padH * 2;
        int h = 8 + padV * 2;
        drawRoundedRect(ex, x, y, w, h, h / 2, bg);
        if (border != 0) {
            drawRoundedOutline(ex, x, y, w, h, h / 2, 1, border);
        }
        ex.text(font, text, x + padH, y + padV, textColor, false);
    }

    /** Texto con sombra limpia y suave. */
    public static void textShadowed(GuiGraphicsExtractor ex, Font font, String text, int x, int y, int color) {
        ex.text(font, text, x + 1, y + 1, 0x99000000, false);
        ex.text(font, text, x, y, color, false);
    }

    /** Centra texto con sombra. */
    public static void textCenteredShadowed(GuiGraphicsExtractor ex, Font font, String text, int cx, int y, int color) {
        int w = font.width(text);
        textShadowed(ex, font, text, cx - w / 2, y, color);
    }

    /** Interpola linealmente entre dos colores ARGB. */
    public static int interpolateColor(int c1, int c2, float t) {
        t = Math.max(0f, Math.min(1f, t));
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = (int) (a1 + (a2 - a1) * t);
        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}