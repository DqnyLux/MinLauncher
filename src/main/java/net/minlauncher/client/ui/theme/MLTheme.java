package net.minlauncher.client.ui.theme;

/**
 * Identidad visual y paleta de MinLauncher (estilo Lunar / Badlion moderno).
 * Paleta refinada: Amarillo Sol Radiante + Oro Dorado + Ámbar Lujoso + Blanco Puro
 * sobre fondos oscuros frosted glass con resplandores sutiles dorados.
 */
public final class MLTheme {
    private MLTheme() {}

    // ---- Acentos Principales (Amarillo y Dorado) ----
    public static final int ACCENT          = 0xFFF59E0B; // Ámbar Dorado primario (#F59E0B)
    public static final int ACCENT_BRIGHT   = 0xFFFDE047; // Amarillo Sol brillante (#FDE047)
    public static final int ACCENT_DIM      = 0xFFD97706; // Dorado Ocre profundo (#D97706)
    public static final int ACCENT_DARK     = 0xFF92400E; // Ámbar oscuro (#92400E)
    public static final int ACCENT_GLOW     = 0x44F59E0B; // Resplandor dorado suave

    public static final int GOLD            = 0xFFFBBF24; // Oro Puro (#FBBF24)
    public static final int GOLD_BRIGHT     = 0xFFFEF08A; // Oro Claro brillante (#FEF08A)
    public static final int GOLD_DIM        = 0xFFB45309; // Oro Bronce (#B45309)
    public static final int GOLD_GLOW       = 0x44FBBF24; // Resplandor oro suave

    public static final int WHITE           = 0xFFFFFFFF;
    public static final int WHITE_DIM       = 0xFFF1F5F9;

    // ---- Fondos Modernos Translúcidos (Transparentes Glass / Acrílico) ----
    public static final int BG_DARKEST      = 0x7008090E; // Fondo translúcido oscuro
    public static final int BG_DARK         = 0x800E1017; // Fondo translúcido profundo
    public static final int BG_PANEL        = 0x90131622; // Panel vidrio acrílico transparente
    public static final int BG_CARD         = 0x751A1E2E; // Tarjeta interna semitransparente
    public static final int BG_CARD_HOVER   = 0x95262B40; // Tarjeta en hover
    public static final int BG_FEATURED     = 0x9578350F; // Tarjeta destacada dorada/ámbar
    public static final int BG_FEATURED_HOV = 0xB592400E; // Tarjeta destacada hover

    // ---- Bordes y Líneas ----
    public static final int BORDER_SUBTLE   = 0x22F59E0B; // Borde muy tenue dorado
    public static final int BORDER_DEFAULT  = 0x44F59E0B; // Borde por defecto dorado
    public static final int BORDER_HOVER    = 0xCCFBBF24; // Borde activo oro
    public static final int BORDER_GOLD     = 0xAAFBBF24; // Borde oro radiante

    // ---- Texto Tipográfico ----
    public static final int TEXT_PRIMARY    = 0xFFF8FAFC; // Blanco suave alta legibilidad
    public static final int TEXT_SECONDARY  = 0xFFE2E8F0; // Gris claro
    public static final int TEXT_DIM        = 0xFF94A3B8; // Gris medio
    public static final int TEXT_MUTED      = 0xFF64748B; // Gris sutil

    // ---- Sombras y Utilidades ----
    public static final int SHADOW_DARK     = 0xB0000000;
    public static final int SHADOW_SOFT     = 0x60000000;

    // ---- Metadatos del Cliente ----
    public static final String NAME         = "MinLauncher";
    public static final String VERSION      = "1.0.0";
    public static final String EDITION      = "MinLauncher Edition";
    public static final String TAGLINE      = "CLIENTE DE ALTO RENDIMIENTO · 100% LEGAL";

    // ---- Colores Dinámicos & Suaves (Transiciones continuas) ----
    public static int getSmoothAccent() {
        long time = System.currentTimeMillis();
        float t = (time % 3500L) / 3500f * (float)(Math.PI * 2);
        int r1 = 0xFD, g1 = 0xE0, b1 = 0x47; // Amarillo Sol #FDE047
        int r2 = 0xF5, g2 = 0x9E, b2 = 0x0B; // Ámbar Dorado #F59E0B
        float factor = (float)(Math.sin(t) + 1f) / 2f;
        int r = (int)(r1 + (r2 - r1) * factor);
        int g = (int)(g1 + (g2 - g1) * factor);
        int b = (int)(b1 + (b2 - b1) * factor);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public static int getRainbow(float offset) {
        float hue = ((System.currentTimeMillis() + (long)(offset * 1000f)) % 3000L) / 3000f;
        return java.awt.Color.HSBtoRGB(hue, 0.8f, 0.9f) | 0xFF000000;
    }
}
