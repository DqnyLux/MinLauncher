package net.minlauncher.client.ui.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.ui.ClickGuiScreen;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;

/**
 * Menú de pausa de MinLauncher: estilo compacto y moderno alineado con el título
 * en tonos Dorados, Ámbar y Fondos Frosted Glass.
 */
public class MinLauncherPauseScreen extends Screen {

    private record MenuItem(String label, String icon, Runnable action, boolean featured) {}
    private MenuItem[] menu;
    private int panelX, panelY, panelW, panelH;

    public MinLauncherPauseScreen() {
        super(Component.literal(MLTheme.NAME + " · Pausa"));
    }

    @Override
    protected void init() {
        super.init();
        int itemW = 180, itemH = 20, gap = 4;
        int count = 4;
        int pad = 10;
        panelW = itemW + pad * 2;
        panelH = pad * 2 + count * itemH + (count - 1) * gap;
        panelY = Math.max(50, (this.height - panelH) / 2 + 8);
        panelX = (this.width - panelW) / 2;

        menu = new MenuItem[] {
            new MenuItem("Reanudar juego", "▶", () -> minecraft.setScreen(null), false),
            new MenuItem("Módulos & Ajustes (RSHIFT)", "✦", () -> minecraft.setScreen(new ClickGuiScreen()), true),
            new MenuItem("Opciones de Minecraft", "⚙", () -> minecraft.setScreen(new OptionsScreen(this, minecraft.options, true)), false),
            new MenuItem("Guardar y salir al título", "✕", () -> minecraft.disconnect(this, false, false), false),
        };
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        // Fondo semitransparente oscuro con resplandor suave
        RenderUtils.drawGradientV(g, 0, 0, this.width, this.height, 0x600B1120, 0x70050811);

        // Cabecera PAUSA centrada
        String title = "PAUSA DEL JUEGO";
        int tw = font.width(title);
        RenderUtils.textShadowed(g, font, title, this.width / 2 - tw / 2, panelY - 34, MLTheme.WHITE);
        RenderUtils.drawGradientH(g, this.width / 2 - 30, panelY - 22, 60, 2, MLTheme.ACCENT, MLTheme.GOLD);

        // Panel central frosted glass
        RenderUtils.drawShadow(g, panelX, panelY, panelW, panelH, 6, MLTheme.SHADOW_DARK);
        RenderUtils.drawRoundedRect(g, panelX, panelY, panelW, panelH, 8, MLTheme.BG_PANEL);
        RenderUtils.drawRoundedOutline(g, panelX, panelY, panelW, panelH, 8, 1, MLTheme.BORDER_DEFAULT);

        // Botones
        int pad = 10, itemH = 20, gap = 4;
        int itemX = panelX + pad, itemW = panelW - (pad * 2);
        for (int i = 0; i < menu.length; i++) {
            int y = panelY + pad + i * (itemH + gap);
            boolean hover = mouseX >= itemX && mouseX <= itemX + itemW && mouseY >= y && mouseY <= y + itemH;
            MenuItem item = menu[i];

            int bg, border, textColor, iconColor;

            if (item.featured()) {
                bg = hover ? MLTheme.BG_FEATURED_HOV : MLTheme.BG_FEATURED;
                border = hover ? MLTheme.GOLD : MLTheme.BORDER_GOLD;
                textColor = MLTheme.WHITE;
                iconColor = MLTheme.GOLD_BRIGHT;
                if (hover) {
                    RenderUtils.drawGlow(g, itemX, y, itemW, itemH, 5, 2, MLTheme.GOLD_GLOW);
                }
            } else {
                bg = hover ? MLTheme.BG_CARD_HOVER : MLTheme.BG_CARD;
                border = hover ? MLTheme.BORDER_HOVER : MLTheme.BORDER_SUBTLE;
                textColor = hover ? MLTheme.WHITE : MLTheme.TEXT_PRIMARY;
                iconColor = hover ? MLTheme.ACCENT_BRIGHT : MLTheme.TEXT_DIM;
                if (hover) {
                    RenderUtils.drawGlow(g, itemX, y, itemW, itemH, 5, 2, MLTheme.ACCENT_GLOW);
                }
            }

            RenderUtils.drawRoundedRect(g, itemX, y, itemW, itemH, 5, bg);
            RenderUtils.drawRoundedOutline(g, itemX, y, itemW, itemH, 5, 1, border);

            // Icono
            g.text(font, item.icon(), itemX + 10, y + (itemH - 8) / 2, iconColor, false);

            // Label centrado
            int lw = font.width(item.label());
            g.text(font, item.label(), itemX + (itemW - lw) / 2 + 4, y + (itemH - 8) / 2, textColor, false);
        }

        super.extractRenderState(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean captured) {
        int mx = (int) event.x(), my = (int) event.y();
        int pad = 10, itemH = 20, gap = 4;
        int itemX = panelX + pad, itemW = panelW - (pad * 2);
        for (int i = 0; i < menu.length; i++) {
            int y = panelY + pad + i * (itemH + gap);
            if (mx >= itemX && mx <= itemX + itemW && my >= y && my <= y + itemH) {
                menu[i].action().run();
                return true;
            }
        }
        return super.mouseClicked(event, captured);
    }
}
