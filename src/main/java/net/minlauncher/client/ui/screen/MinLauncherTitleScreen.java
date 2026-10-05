package net.minlauncher.client.ui.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.ui.ClickGuiScreen;
import net.minlauncher.client.ui.CosmeticsScreen;
import net.minlauncher.client.ui.HudEditorScreen;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Pantalla principal de MinLauncher: diseño ultra moderno estilo Lunar / Badlion Client.
 * Fondo acrílico oscuro profundo, acentos en Oro Dorado (#FBBF24), Ámbar (#F59E0B) y Amarillo Sol (#FDE047),
 * tarjetas de navegación con esquinas redondeadas reales y efectos de resplandor.
 */
public class MinLauncherTitleScreen extends Screen {

    private static final String[] SPLASHES = {
        "Cliente optimizado · 100% Legal",
        "Rendimiento puro · Sin hacks",
        "PvP & Survival con estilo",
        "Personalización total del HUD",
        "Presiona RSHIFT para abrir módulos",
    };

    private final String splash;
    private float fade = 0f;

    private record ActionButton(String title, String subtitle, String icon, Runnable action, boolean featured, int x, int y, int w, int h) {}
    private ActionButton[] buttons;

    // Dimensiones del panel central
    private int panelX, panelY, panelW, panelH;

    public MinLauncherTitleScreen() {
        super(Component.literal(MLTheme.NAME));
        this.splash = SPLASHES[ThreadLocalRandom.current().nextInt(SPLASHES.length)];
    }

    @Override
    protected void init() {
        super.init();

        // Responsive sizing más compacto y limpio
        int baseW = 270;
        int maxW = Math.max(210, this.width - 40);
        panelW = Math.min(baseW, maxW);

        int availableH = this.height - 80;
        int compactThreshold = 240;
        boolean compact = availableH < compactThreshold;

        panelH = compact ? 128 : 148;
        panelX = (this.width - panelW) / 2;

        int idealY = (this.height - panelH) / 2 + (compact ? 10 : 16);
        panelY = Math.max(compact ? 42 : 56, idealY);

        int bx = panelX + 10;
        int bw = panelW - 20;
        int by = panelY + 8;

        int hRow = compact ? 19 : 22;
        int hFeatured = compact ? 21 : 24;
        int hBottom = compact ? 18 : 20;
        int spacing = compact ? 3 : 5;

        // 1. Un Jugador (Hero Card)
        ActionButton bSingle = new ActionButton("Un jugador", compact ? null : "Mundos locales & supervivencia", "▶",
            () -> minecraft.setScreen(new SelectWorldScreen(this)), false, bx, by, bw, hRow);
        by += hRow + spacing;

        // 2. Multijugador (Hero Card)
        ActionButton bMulti = new ActionButton("Multijugador", compact ? null : "Servidores PvP y comunidad", "🌐",
            () -> minecraft.setScreen(new JoinMultiplayerScreen(this)), false, bx, by, bw, hRow);
        by += hRow + spacing;

        // 3. Módulos del Cliente (Featured Card con resplandor dorado)
        ActionButton bMods = new ActionButton("Módulos & Ajustes (RSHIFT)", compact ? null : "Configurar HUD, PvP y Survival", "✦",
            () -> minecraft.setScreen(new ClickGuiScreen()), true, bx, by, bw, hFeatured);
        by += hFeatured + spacing;

        // 4. Opciones y Salir (Dos columnas inferiores)
        int halfW = (bw - 4) / 2;
        ActionButton bOptions = new ActionButton("Opciones", null, "⚙",
            () -> minecraft.setScreen(new OptionsScreen(this, minecraft.options, false)), false, bx, by, halfW, hBottom);
        ActionButton bQuit = new ActionButton("Salir", null, "✕",
            () -> minecraft.stop(), false, bx + halfW + 4, by, halfW, hBottom);

        buttons = new ActionButton[] { bSingle, bMulti, bMods, bOptions, bQuit };
    }

    @Override
    public void tick() {
        fade = Math.min(fade + 0.05f, 1f);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        int alpha = (int) (fade * 255);

        // ==========================================
        // 1. FONDO PROFUNDO CON RESPLANDORES DORADOS
        // ==========================================
        RenderUtils.drawGradientV(g, 0, 0, this.width, this.height, MLTheme.BG_DARK, MLTheme.BG_DARKEST);

        // Cono de luz superior ámbar dorado
        g.fillGradient(0, 0, this.width, 120, 0x22F59E0B, 0x00F59E0B);
        // Cono de luz inferior amarillo cálido
        g.fillGradient(0, this.height - 100, this.width, this.height, 0x00FBBF24, 0x16FBBF24);

        // ==========================================
        // 2. BARRA SUPERIOR (HEADER RÁPIDO)
        // ==========================================
        // Logo miniatura arriba a la izquierda
        RenderUtils.drawPill(g, font, "✦ MINLAUNCHER", 14, 10, 0x660B1120, MLTheme.BORDER_SUBTLE, MLTheme.GOLD);
        RenderUtils.drawPill(g, font, "v" + MLTheme.VERSION, 108, 10, 0x4478350F, MLTheme.BORDER_DEFAULT, MLTheme.ACCENT_BRIGHT);

        // Botones rápidos arriba a la derecha
        int topBtnY = 10;
        int topBtnH = 14;

        // Botón Cosméticos
        int cosW = font.width("Armario") + 14;
        int cosX = this.width - cosW - 14;
        boolean cosHov = mouseX >= cosX && mouseX <= cosX + cosW && mouseY >= topBtnY && mouseY <= topBtnY + topBtnH;
        RenderUtils.drawRoundedRect(g, cosX, topBtnY, cosW, topBtnH, 4, cosHov ? 0xDD334155 : 0x881E293B);
        RenderUtils.drawRoundedOutline(g, cosX, topBtnY, cosW, topBtnH, 4, 1, cosHov ? MLTheme.GOLD : MLTheme.BORDER_SUBTLE);
        g.text(font, "Armario", cosX + 7, topBtnY + 3, cosHov ? MLTheme.WHITE : MLTheme.TEXT_SECONDARY, false);

        // Botón Editor HUD
        int hudW = font.width("Editor HUD") + 14;
        int hudX = cosX - hudW - 6;
        boolean hudHov = mouseX >= hudX && mouseX <= hudX + hudW && mouseY >= topBtnY && mouseY <= topBtnY + topBtnH;
        RenderUtils.drawRoundedRect(g, hudX, topBtnY, hudW, topBtnH, 4, hudHov ? 0xDD334155 : 0x881E293B);
        RenderUtils.drawRoundedOutline(g, hudX, topBtnY, hudW, topBtnH, 4, 1, hudHov ? MLTheme.GOLD : MLTheme.BORDER_SUBTLE);
        g.text(font, "Editor HUD", hudX + 7, topBtnY + 3, hudHov ? MLTheme.WHITE : MLTheme.TEXT_SECONDARY, false);

        // ==========================================
        // 3. LOGO HERO CENTRADO (Dorado Dinámico Suave)
        // ==========================================
        int logoY = Math.max(24, panelY - 42);
        String part1 = "MIN", part2 = "LAUNCHER";
        int w1 = font.width(part1), w2 = font.width(part2);
        int totalLogoW = w1 + w2;

        int dynamicAccent = MLTheme.getSmoothAccent();

        g.pose().pushMatrix();
        g.pose().translate(this.width / 2f, logoY);
        float scale = this.height < 300 ? 1.6f : 2.0f;
        g.pose().scale(scale, scale);
        RenderUtils.textShadowed(g, font, part1, -totalLogoW / 2, -6, dynamicAccent);
        RenderUtils.textShadowed(g, font, part2, -totalLogoW / 2 + w1, -6, MLTheme.WHITE);
        g.pose().popMatrix();

        // Línea divisoria luminosa bajo el logo
        int lineW = Math.min(120, panelW / 2);
        RenderUtils.drawGradientH(g, this.width / 2 - lineW / 2, logoY + 12, lineW / 2, 2, 0x00F59E0B, dynamicAccent);
        RenderUtils.drawGradientH(g, this.width / 2, logoY + 12, lineW / 2, 2, dynamicAccent, 0x00FBBF24);

        // Subtítulo con splash en píldora
        int splashW = font.width(splash) + 16;
        int splashX = this.width / 2 - splashW / 2;
        int splashY = logoY + 18;
        RenderUtils.drawRoundedRect(g, splashX, splashY, splashW, 13, 6, 0xAA78350F);
        RenderUtils.drawRoundedOutline(g, splashX, splashY, splashW, 13, 6, 1, MLTheme.BORDER_DEFAULT);
        g.text(font, splash, splashX + 8, splashY + 3, MLTheme.GOLD_BRIGHT, false);

        // ==========================================
        // 4. PANEL CENTRAL FROSTED GLASS
        // ==========================================
        RenderUtils.drawShadow(g, panelX, panelY, panelW, panelH, 6, MLTheme.SHADOW_DARK);
        RenderUtils.drawRoundedRect(g, panelX, panelY, panelW, panelH, 8, MLTheme.BG_PANEL);
        RenderUtils.drawRoundedOutline(g, panelX, panelY, panelW, panelH, 8, 1, MLTheme.BORDER_DEFAULT);

        // ==========================================
        // 5. BOTONES DE ACCIÓN
        // ==========================================
        for (ActionButton btn : buttons) {
            boolean hover = mouseX >= btn.x() && mouseX <= btn.x() + btn.w() &&
                            mouseY >= btn.y() && mouseY <= btn.y() + btn.h();

            int bg, border, textColor, iconColor;

            if (btn.featured()) {
                // Botón destacado de módulos (Dorado / Ámbar radiante)
                bg = hover ? MLTheme.BG_FEATURED_HOV : MLTheme.BG_FEATURED;
                border = hover ? MLTheme.GOLD : MLTheme.BORDER_GOLD;
                textColor = MLTheme.WHITE;
                iconColor = MLTheme.GOLD_BRIGHT;
                if (hover) {
                    RenderUtils.drawGlow(g, btn.x(), btn.y(), btn.w(), btn.h(), 6, 2, MLTheme.GOLD_GLOW);
                }
            } else {
                // Botones estándar
                bg = hover ? MLTheme.BG_CARD_HOVER : MLTheme.BG_CARD;
                border = hover ? MLTheme.BORDER_HOVER : MLTheme.BORDER_SUBTLE;
                textColor = hover ? MLTheme.WHITE : MLTheme.TEXT_PRIMARY;
                iconColor = hover ? MLTheme.ACCENT_BRIGHT : MLTheme.TEXT_DIM;
                if (hover) {
                    RenderUtils.drawGlow(g, btn.x(), btn.y(), btn.w(), btn.h(), 5, 2, MLTheme.ACCENT_GLOW);
                }
            }

            // Fondo y borde redondeado
            RenderUtils.drawRoundedRect(g, btn.x(), btn.y(), btn.w(), btn.h(), 5, bg);
            RenderUtils.drawRoundedOutline(g, btn.x(), btn.y(), btn.w(), btn.h(), 5, 1, border);

            // Icono a la izquierda
            g.text(font, btn.icon(), btn.x() + 10, btn.y() + (btn.subtitle() != null ? 6 : (btn.h() - 8) / 2), iconColor, false);

            // Texto y subtítulo
            if (btn.subtitle() != null) {
                g.text(font, btn.title(), btn.x() + 24, btn.y() + 5, textColor, false);
                g.text(font, btn.subtitle(), btn.x() + 24, btn.y() + 15, MLTheme.TEXT_MUTED, false);
            } else {
                // Botón simple centrado
                int tw = font.width(btn.title());
                g.text(font, btn.title(), btn.x() + (btn.w() - tw) / 2 + 4, btn.y() + (btn.h() - 8) / 2, textColor, false);
            }

            // Indicador chevron a la derecha en hover
            if (hover && btn.subtitle() != null) {
                g.text(font, "›", btn.x() + btn.w() - 14, btn.y() + (btn.h() - 8) / 2, iconColor, false);
            }
        }

        // ==========================================
        // 6. FOOTER INFORMATIVO
        // ==========================================
        String footLeft = "Minecraft 26.1.2  ·  " + MLTheme.EDITION;
        String footRight = MLTheme.NAME + " v" + MLTheme.VERSION + "  ·  Listo para jugar";
        g.text(font, footLeft, 14, this.height - 14, (alpha << 24) | (MLTheme.TEXT_MUTED & 0xFFFFFF), false);
        int frw = font.width(footRight);
        g.text(font, footRight, this.width - frw - 14, this.height - 14, (alpha << 24) | (MLTheme.TEXT_MUTED & 0xFFFFFF), false);

        super.extractRenderState(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean captured) {
        int mx = (int) event.x(), my = (int) event.y();

        // Botones de la barra superior
        int topBtnY = 10, topBtnH = 14;
        int cosW = font.width("Armario") + 14;
        int cosX = this.width - cosW - 14;
        if (mx >= cosX && mx <= cosX + cosW && my >= topBtnY && my <= topBtnY + topBtnH) {
            minecraft.setScreen(new CosmeticsScreen());
            return true;
        }

        int hudW = font.width("Editor HUD") + 14;
        int hudX = cosX - hudW - 6;
        if (mx >= hudX && mx <= hudX + hudW && my >= topBtnY && my <= topBtnY + topBtnH) {
            minecraft.setScreen(new HudEditorScreen());
            return true;
        }

        // Botones del panel central
        for (ActionButton btn : buttons) {
            if (mx >= btn.x() && mx <= btn.x() + btn.w() && my >= btn.y() && my <= btn.y() + btn.h()) {
                btn.action().run();
                return true;
            }
        }

        return super.mouseClicked(event, captured);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
