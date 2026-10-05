package net.minlauncher.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ColorSetting;
import net.minlauncher.client.module.setting.ModeSetting;
import net.minlauncher.client.module.setting.NumberSetting;
import net.minlauncher.client.module.setting.Setting;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.screen.MinLauncherTitleScreen;
import net.minlauncher.client.ui.theme.MLTheme;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard de Módulos de MinLauncher estilo Lunar Client / Badlion Client:
 * Ventana central moderna tipo frosted glass con pestañas de categoría superiores,
 * buscador en tiempo real, grid de tarjetas con switches animados estilo píldora
 * y panel de configuración detallado con sliders táctiles y selectores.
 */
public class ClickGuiScreen extends Screen {

    // Categoría seleccionada (null = Todos)
    private Category selectedCategory = null;
    private String searchQuery = "";
    private boolean searchFocused = false;

    // Módulo seleccionado para ver sus ajustes detallados (null = vista grid general)
    private Module selectedModuleForSettings = null;

    // Scroll del grid
    private int scrollOffset = 0;
    private int maxScroll = 0;

    // Arrastre de slider de ajuste
    private NumberSetting draggingSlider = null;
    private int sliderStartX, sliderWidth;

    public ClickGuiScreen() {
        super(Component.literal(MLTheme.NAME + " · Módulos"));
    }

    @Override
    protected void init() {
        super.init();
        scrollOffset = 0;
    }

    // ==========================================
    // RENDER PRINCIPAL
    // ==========================================

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        // 1. Fondo oscurecido con viñeta sutil y transparente
        RenderUtils.drawGradientV(g, 0, 0, width, height, 0x6008090E, 0x750E1017);

        // 2. Dimensiones compactas y equilibradas del Dashboard Modal
        int modalW = Math.min(520, width - 20);
        int modalH = Math.min(320, height - 24);
        int modalX = (width - modalW) / 2;
        int modalY = (height - modalH) / 2;

        // 3. Sombra y Panel Frosted Glass Central
        RenderUtils.drawShadow(g, modalX, modalY, modalW, modalH, 6, MLTheme.SHADOW_DARK);
        RenderUtils.drawRoundedRect(g, modalX, modalY, modalW, modalH, 7, MLTheme.BG_PANEL);
        RenderUtils.drawRoundedOutline(g, modalX, modalY, modalW, modalH, 7, 1, MLTheme.BORDER_DEFAULT);

        // 4. Header Superior del Modal (Logo + Buscador + Pestañas + Acciones)
        int headerH = 34;
        RenderUtils.drawRoundedRect(g, modalX + 1, modalY + 1, modalW - 2, headerH, 6, 0x800E1017);
        RenderUtils.drawGradientH(g, modalX + 6, modalY + headerH, modalW - 12, 1, MLTheme.ACCENT, MLTheme.GOLD);

        // Logo del cliente
        int smoothAcc = MLTheme.getSmoothAccent();
        g.text(font, "✦ MINLAUNCHER", modalX + 10, modalY + 8, smoothAcc, true);
        g.text(font, "v" + MLTheme.VERSION, modalX + 10, modalY + 19, MLTheme.TEXT_MUTED, false);

        // Barra de búsqueda interactiva
        int searchW = 100;
        int searchH = 16;
        int searchX = modalX + 105;
        int searchY = modalY + 9;
        boolean hoverSearch = mouseX >= searchX && mouseX <= searchX + searchW && mouseY >= searchY && mouseY <= searchY + searchH;
        int searchBg = (searchFocused || hoverSearch) ? 0xDD1E293B : 0x8814151F;
        int searchBorder = searchFocused ? MLTheme.GOLD : (hoverSearch ? MLTheme.BORDER_HOVER : MLTheme.BORDER_SUBTLE);
        RenderUtils.drawRoundedRect(g, searchX, searchY, searchW, searchH, 4, searchBg);
        RenderUtils.drawRoundedOutline(g, searchX, searchY, searchW, searchH, 4, 1, searchBorder);

        String searchDisplay = searchQuery.isEmpty() ? (searchFocused ? "" : "🔍 Buscar...") : searchQuery;
        int searchColor = searchQuery.isEmpty() ? MLTheme.TEXT_MUTED : MLTheme.WHITE;
        g.text(font, searchDisplay, searchX + 5, searchY + 4, searchColor, false);

        // Botón rápido Editor HUD
        int hudBtnW = 68;
        int hudBtnH = 16;
        int hudBtnX = modalX + modalW - hudBtnW - 28;
        int hudBtnY = modalY + 9;
        boolean hoverHud = mouseX >= hudBtnX && mouseX <= hudBtnX + hudBtnW && mouseY >= hudBtnY && mouseY <= hudBtnY + hudBtnH;
        RenderUtils.drawRoundedRect(g, hudBtnX, hudBtnY, hudBtnW, hudBtnH, 4, hoverHud ? 0xDD334155 : 0x881E293B);
        RenderUtils.drawRoundedOutline(g, hudBtnX, hudBtnY, hudBtnW, hudBtnH, 4, 1, hoverHud ? MLTheme.GOLD : MLTheme.BORDER_SUBTLE);
        g.text(font, "☩ Editor HUD", hudBtnX + 5, hudBtnY + 4, hoverHud ? MLTheme.GOLD_BRIGHT : MLTheme.TEXT_SECONDARY, false);

        // Botón Cerrar (✕)
        int closeX = modalX + modalW - 20;
        int closeY = modalY + 10;
        boolean hoverClose = mouseX >= closeX && mouseX <= closeX + 14 && mouseY >= closeY && mouseY <= closeY + 14;
        g.text(font, "✕", closeX + 2, closeY + 1, hoverClose ? MLTheme.GOLD_BRIGHT : MLTheme.TEXT_MUTED, false);

        // Pestañas de categoría horizontales (debajo del header)
        int tabY = modalY + headerH + 4;
        int tabH = 18;
        renderCategoryTabs(g, modalX + 8, tabY, modalW - 16, tabH, mouseX, mouseY);

        // 5. Contenido Central (Grid de Tarjetas o Vista de Ajustes)
        int contentY = tabY + tabH + 6;
        int contentH = modalH - (contentY - modalY) - 22;
        int contentW = modalW - 16;
        int contentX = modalX + 8;

        if (selectedModuleForSettings != null) {
            renderModuleSettingsView(g, selectedModuleForSettings, contentX, contentY, contentW, contentH, mouseX, mouseY);
        } else {
            renderModuleCardsGrid(g, contentX, contentY, contentW, contentH, mouseX, mouseY);
        }

        // 6. Footer del Modal
        int footerY = modalY + modalH - 16;
        String hint = "RSHIFT o ESC para cerrar  ·  Clic para activar/desactivar";
        int hw = font.width(hint);
        g.text(font, hint, modalX + (modalW - hw) / 2, footerY + 2, MLTheme.TEXT_MUTED, false);

        super.extractRenderState(g, mouseX, mouseY, delta);
    }

    // ==========================================
    // PESTAÑAS DE CATEGORÍA
    // ==========================================

    private void renderCategoryTabs(GuiGraphicsExtractor g, int x, int y, int w, int h, int mouseX, int mouseY) {
        List<CategoryTab> tabs = new ArrayList<>();
        tabs.add(new CategoryTab(null, "✦ Todos"));
        for (Category c : Category.values()) {
            String icon = switch (c) {
                case PVP -> "⚔ ";
                case SURVIVAL -> "⚡ ";
                case HUD -> "📊 ";
                case RENDER -> "👁 ";
                case COSMETICS -> "👕 ";
            };
            tabs.add(new CategoryTab(c, icon + c.getDisplayName()));
        }

        int tabW = (w - (tabs.size() - 1) * 4) / tabs.size();
        for (int i = 0; i < tabs.size(); i++) {
            CategoryTab tab = tabs.get(i);
            int tx = x + i * (tabW + 4);
            boolean isSelected = (selectedCategory == tab.category && selectedModuleForSettings == null);
            boolean hover = mouseX >= tx && mouseX <= tx + tabW && mouseY >= y && mouseY <= y + h;

            int bg = isSelected ? MLTheme.BG_FEATURED : (hover ? MLTheme.BG_CARD_HOVER : 0xAA14151F);
            int border = isSelected ? MLTheme.BORDER_GOLD : (hover ? MLTheme.BORDER_HOVER : MLTheme.BORDER_SUBTLE);
            int textCol = isSelected ? MLTheme.WHITE : (hover ? MLTheme.WHITE : MLTheme.TEXT_DIM);

            RenderUtils.drawRoundedRect(g, tx, y, tabW, h, 4, bg);
            RenderUtils.drawRoundedOutline(g, tx, y, tabW, h, 4, 1, border);

            int tw = font.width(tab.label);
            g.text(font, tab.label, tx + (tabW - tw) / 2, y + 6, textCol, isSelected);
        }
    }

    private record CategoryTab(Category category, String label) {}

    // ==========================================
    // GRID DE TARJETAS DE MÓDULOS
    // ==========================================

    private void renderModuleCardsGrid(GuiGraphicsExtractor g, int x, int y, int w, int h, int mouseX, int mouseY) {
        List<Module> allMods = MinLauncher.getInstance().getModuleManager().getModules();
        List<Module> filtered = new ArrayList<>();

        for (Module m : allMods) {
            if (selectedCategory != null && m.getCategory() != selectedCategory) continue;
            if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;
            filtered.add(m);
        }

        int cols = w > 360 ? 2 : 1;
        int cardGap = 6;
        int cardW = (w - (cols - 1) * cardGap) / cols;
        int cardH = 36;

        int totalRows = (int) Math.ceil((double) filtered.size() / cols);
        int totalContentH = totalRows * (cardH + cardGap);
        maxScroll = Math.max(0, totalContentH - h);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        for (int i = 0; i < filtered.size(); i++) {
            Module m = filtered.get(i);
            int col = i % cols;
            int row = i / cols;

            int cx = x + col * (cardW + cardGap);
            int cy = y + row * (cardH + cardGap) - scrollOffset;

            // Culling visual
            if (cy + cardH < y || cy > y + h) continue;

            boolean on = m.isEnabled();
            boolean hover = mouseX >= cx && mouseX <= cx + cardW && mouseY >= cy && mouseY <= cy + cardH;

            int bg = hover ? MLTheme.BG_CARD_HOVER : MLTheme.BG_CARD;
            int border = on ? MLTheme.BORDER_HOVER : (hover ? MLTheme.BORDER_DEFAULT : MLTheme.BORDER_SUBTLE);

            // Tarjeta redondeada
            RenderUtils.drawRoundedRect(g, cx, cy, cardW, cardH, 6, bg);
            RenderUtils.drawRoundedOutline(g, cx, cy, cardW, cardH, 6, 1, border);

            // Icono de categoría
            String catIcon = switch (m.getCategory()) {
                case PVP -> "⚔";
                case SURVIVAL -> "⚡";
                case HUD -> "📊";
                case RENDER -> "👁";
                case COSMETICS -> "👕";
            };
            g.text(font, catIcon, cx + 10, cy + 12, on ? MLTheme.GOLD_BRIGHT : MLTheme.TEXT_MUTED, false);

            // Nombre del módulo y categoría
            g.text(font, m.getName(), cx + 24, cy + 10, on ? MLTheme.WHITE : MLTheme.TEXT_SECONDARY, true);
            g.text(font, m.getCategory().getDisplayName(), cx + 24, cy + 24, MLTheme.TEXT_MUTED, false);

            // Switch animado estilo píldora
            int swW = 26, swH = 14;
            int swX = cx + cardW - swW - 10, swY = cy + 10;
            int swBg = on ? MLTheme.ACCENT : 0xAA334155;
            RenderUtils.drawRoundedRect(g, swX, swY, swW, swH, 7, swBg);
            int dotX = on ? swX + swW - 12 : swX + 2;
            RenderUtils.drawRoundedRect(g, dotX, swY + 2, 10, 10, 5, MLTheme.WHITE);
            if (on) {
                RenderUtils.drawGlow(g, swX, swY, swW, swH, 5, 1, MLTheme.ACCENT_GLOW);
            }

            // Botón de engranaje ⚙ Ajustes si el módulo tiene settings
            if (!m.getSettings().isEmpty()) {
                int gearX = swX - 22;
                int gearY = cy + 9;
                boolean hoverGear = mouseX >= gearX && mouseX <= gearX + 16 && mouseY >= gearY && mouseY <= gearY + 16;
                g.text(font, "⚙", gearX + 3, gearY + 3, hoverGear ? MLTheme.GOLD_BRIGHT : MLTheme.TEXT_DIM, false);
            }
        }
    }

    // ==========================================
    // VISTA DETALLADA DE AJUSTES DE UN MÓDULO
    // ==========================================

    private void renderModuleSettingsView(GuiGraphicsExtractor g, Module m, int x, int y, int w, int h, int mouseX, int mouseY) {
        // Barra superior con botón volver
        int backBtnW = 100;
        int backBtnH = 18;
        boolean hoverBack = mouseX >= x && mouseX <= x + backBtnW && mouseY >= y && mouseY <= y + backBtnH;
        RenderUtils.drawRoundedRect(g, x, y, backBtnW, backBtnH, 4, hoverBack ? 0xDD334155 : 0x881E293B);
        RenderUtils.drawRoundedOutline(g, x, y, backBtnW, backBtnH, 4, 1, hoverBack ? MLTheme.GOLD : MLTheme.BORDER_SUBTLE);
        g.text(font, "‹ Volver a mods", x + 8, y + 5, hoverBack ? MLTheme.GOLD_BRIGHT : MLTheme.TEXT_SECONDARY, false);

        // Nombre del módulo y Switch principal
        g.text(font, "✦ " + m.getName(), x + backBtnW + 14, y + 4, MLTheme.WHITE, true);
        boolean on = m.isEnabled();
        int swW = 26, swH = 14;
        int swX = x + w - swW - 10, swY = y + 2;
        int swBg = on ? MLTheme.ACCENT : 0xAA334155;
        RenderUtils.drawRoundedRect(g, swX, swY, swW, swH, 7, swBg);
        int dotX = on ? swX + swW - 12 : swX + 2;
        RenderUtils.drawRoundedRect(g, dotX, swY + 2, 10, 10, 5, MLTheme.WHITE);

        // Lista de settings
        int listY = y + backBtnH + 10;
        int listH = h - backBtnH - 10;
        int rowH = 26;
        int gap = 6;

        List<Setting<?>> settings = m.getSettings();
        for (int i = 0; i < settings.size(); i++) {
            Setting<?> s = settings.get(i);
            int sy = listY + i * (rowH + gap);
            if (sy + rowH > y + h) break;

            // Tarjeta de ajuste
            RenderUtils.drawRoundedRect(g, x, sy, w, rowH, 5, MLTheme.BG_CARD);
            RenderUtils.drawRoundedOutline(g, x, sy, w, rowH, 5, 1, MLTheme.BORDER_SUBTLE);

            // Label del ajuste
            g.text(font, s.getName(), x + 10, sy + 9, MLTheme.TEXT_PRIMARY, false);

            // Control interactivo
            if (s instanceof BooleanSetting bool) {
                int bswW = 22, bswH = 12;
                int bswX = x + w - bswW - 12, bswY = sy + 7;
                boolean bon = bool.getValue();
                RenderUtils.drawRoundedRect(g, bswX, bswY, bswW, bswH, 6, bon ? MLTheme.ACCENT : 0xAA334155);
                int bdotX = bon ? bswX + bswW - 10 : bswX + 2;
                RenderUtils.drawRoundedRect(g, bdotX, bswY + 2, 8, 8, 4, MLTheme.WHITE);
            } else if (s instanceof NumberSetting num) {
                double pct = (num.getValue() - num.getMin()) / (num.getMax() - num.getMin());
                int barW = 100, barH = 6;
                int barX = x + w - barW - 55, barY = sy + 10;
                RenderUtils.drawRoundedRect(g, barX, barY, barW, barH, 3, 0xAA1E293B);
                int fillW = (int) Math.round(barW * Math.max(0, Math.min(1, pct)));
                RenderUtils.drawRoundedRect(g, barX, barY, fillW, barH, 3, MLTheme.ACCENT);
                RenderUtils.drawRoundedRect(g, barX + fillW - 3, barY - 2, 6, 10, 3, MLTheme.WHITE);

                String val = num.getIncrement() >= 1 ? String.valueOf(num.getIntValue()) : String.format("%.1f", num.getFloatValue());
                g.text(font, val, barX + barW + 8, sy + 9, MLTheme.GOLD_BRIGHT, false);
            } else if (s instanceof ModeSetting mode) {
                String current = mode.getValue();
                String display = "‹ " + current + " ›";
                int dw = font.width(display);
                g.text(font, display, x + w - dw - 12, sy + 9, MLTheme.GOLD_BRIGHT, false);
            } else if (s instanceof ColorSetting color) {
                int boxSize = 14;
                int boxX = x + w - boxSize - 12, boxY = sy + 6;
                RenderUtils.drawRoundedRect(g, boxX, boxY, boxSize, boxSize, 3, color.getEffectiveColor());
                RenderUtils.drawRoundedOutline(g, boxX, boxY, boxSize, boxSize, 3, 1, MLTheme.WHITE);
                if (color.isRainbow()) {
                    g.text(font, "RGB Chroma", boxX - 68, sy + 9, MLTheme.GOLD_BRIGHT, false);
                }
            }
        }
    }

    // ==========================================
    // INTERACCIÓN DEL RATÓN
    // ==========================================

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean captured) {
        int mx = (int) event.x(), my = (int) event.y();

        int modalW = Math.min(520, width - 20);
        int modalH = Math.min(320, height - 24);
        int modalX = (width - modalW) / 2;
        int modalY = (height - modalH) / 2;

        // Botón Cerrar ✕
        int closeX = modalX + modalW - 20;
        int closeY = modalY + 10;
        if (mx >= closeX && mx <= closeX + 14 && my >= closeY && my <= closeY + 14) {
            closeToCorrectScreen();
            return true;
        }

        // Botón Editor HUD
        int hudBtnW = 68, hudBtnH = 16;
        int hudBtnX = modalX + modalW - hudBtnW - 28, hudBtnY = modalY + 9;
        if (mx >= hudBtnX && mx <= hudBtnX + hudBtnW && my >= hudBtnY && my <= hudBtnY + hudBtnH) {
            minecraft.setScreen(new HudEditorScreen());
            return true;
        }

        // Buscador focus
        int searchW = 100, searchH = 16;
        int searchX = modalX + 105, searchY = modalY + 9;
        searchFocused = (mx >= searchX && mx <= searchX + searchW && my >= searchY && my <= searchY + searchH);

        // Pestañas de categoría
        int headerH = 34;
        int tabY = modalY + headerH + 4;
        int tabH = 18;
        int tabAreaX = modalX + 8;
        int tabAreaW = modalW - 16;

        List<CategoryTab> tabs = new ArrayList<>();
        tabs.add(new CategoryTab(null, "✦ Todos"));
        for (Category c : Category.values()) tabs.add(new CategoryTab(c, c.getDisplayName()));

        int tabW = (tabAreaW - (tabs.size() - 1) * 4) / tabs.size();
        for (int i = 0; i < tabs.size(); i++) {
            int tx = tabAreaX + i * (tabW + 4);
            if (mx >= tx && mx <= tx + tabW && my >= tabY && my <= tabY + tabH) {
                selectedCategory = tabs.get(i).category;
                selectedModuleForSettings = null;
                scrollOffset = 0;
                return true;
            }
        }

        // Clics en la vista de ajustes
        int contentY = tabY + tabH + 6;
        int contentH = modalH - (contentY - modalY) - 22;
        int contentW = modalW - 16;
        int contentX = modalX + 8;

        if (selectedModuleForSettings != null) {
            // Botón volver
            int backBtnW = 100, backBtnH = 18;
            if (mx >= contentX && mx <= contentX + backBtnW && my >= contentY && my <= contentY + backBtnH) {
                selectedModuleForSettings = null;
                return true;
            }

            // Switch principal del módulo
            int swW = 26, swH = 14;
            int swX = contentX + contentW - swW - 10, swY = contentY + 2;
            if (mx >= swX && mx <= swX + swW && my >= swY && my <= swY + swH) {
                selectedModuleForSettings.toggle();
                MinLauncher.getInstance().getConfigManager().saveConfig();
                return true;
            }

            // Clic en items de settings
            int listY = contentY + backBtnH + 10;
            int rowH = 26, gap = 6;
            List<Setting<?>> settings = selectedModuleForSettings.getSettings();
            for (int i = 0; i < settings.size(); i++) {
                int sy = listY + i * (rowH + gap);
                if (mx >= contentX && mx <= contentX + contentW && my >= sy && my <= sy + rowH) {
                    handleSettingClick(settings.get(i), mx, contentX, contentW);
                    return true;
                }
            }
            return true;
        }

        // Clics en el grid de tarjetas de módulos
        List<Module> allMods = MinLauncher.getInstance().getModuleManager().getModules();
        List<Module> filtered = new ArrayList<>();
        for (Module m : allMods) {
            if (selectedCategory != null && m.getCategory() != selectedCategory) continue;
            if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;
            filtered.add(m);
        }

        int cols = contentW > 360 ? 2 : 1;
        int cardGap = 6;
        int cardW = (contentW - (cols - 1) * cardGap) / cols;
        int cardH = 36;

        for (int i = 0; i < filtered.size(); i++) {
            Module m = filtered.get(i);
            int col = i % cols;
            int row = i / cols;
            int cx = contentX + col * (cardW + cardGap);
            int cy = contentY + row * (cardH + cardGap) - scrollOffset;

            if (cy + cardH < contentY || cy > contentY + contentH) continue;

            if (mx >= cx && mx <= cx + cardW && my >= cy && my <= cy + cardH) {
                // Clic en el botón de engranaje ⚙
                if (!m.getSettings().isEmpty()) {
                    int swW = 26;
                    int swX = cx + cardW - swW - 10;
                    int gearX = swX - 22;
                    int gearY = cy + 9;
                    if (mx >= gearX && mx <= gearX + 18 && my >= gearY && my <= gearY + 18) {
                        selectedModuleForSettings = m;
                        return true;
                    }
                }

                // Clic en la tarjeta -> Toggle del módulo
                m.toggle();
                MinLauncher.getInstance().getConfigManager().saveConfig();
                return true;
            }
        }

        return super.mouseClicked(event, captured);
    }

    private void handleSettingClick(Setting<?> s, int mx, int setX, int setW) {
        if (s instanceof BooleanSetting bool) {
            bool.setValue(!bool.getValue());
            MinLauncher.getInstance().getConfigManager().saveConfig();
        } else if (s instanceof ModeSetting mode) {
            mode.cycle();
            MinLauncher.getInstance().getConfigManager().saveConfig();
        } else if (s instanceof NumberSetting num) {
            int barW = 100;
            int barX = setX + setW - barW - 55;
            draggingSlider = num;
            sliderStartX = barX;
            sliderWidth = barW;
            updateSliderValue(num, mx);
        } else if (s instanceof ColorSetting color) {
            color.setRainbow(!color.isRainbow());
            MinLauncher.getInstance().getConfigManager().saveConfig();
        }
    }

    private void updateSliderValue(NumberSetting num, int mx) {
        double pct = (double) (mx - sliderStartX) / (double) sliderWidth;
        pct = Math.max(0.0, Math.min(1.0, pct));
        double val = num.getMin() + pct * (num.getMax() - num.getMin());
        num.setValue(val);
        MinLauncher.getInstance().getConfigManager().saveConfig();
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        draggingSlider = null;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (draggingSlider != null) {
            updateSliderValue(draggingSlider, (int) event.x());
            return true;
        }
        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (selectedModuleForSettings == null) {
            scrollOffset = (int) Math.max(0, Math.min(scrollOffset - verticalAmount * 16, maxScroll));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    // ==========================================
    // TECLADO Y BÚSQUEDA
    // ==========================================

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (searchFocused && event != null) {
            searchQuery += (char) event.codepoint();
            scrollOffset = 0;
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (searchFocused) {
            if (event.key() == GLFW.GLFW_KEY_BACKSPACE) {
                if (!searchQuery.isEmpty()) {
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                    scrollOffset = 0;
                }
                return true;
            } else if (event.key() == GLFW.GLFW_KEY_ENTER || event.isEscape()) {
                searchFocused = false;
                return true;
            }
        }

        if (event.key() == GLFW.GLFW_KEY_RIGHT_SHIFT || event.isEscape()) {
            closeToCorrectScreen();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        closeToCorrectScreen();
    }

    private void closeToCorrectScreen() {
        if (minecraft.level == null) {
            minecraft.setScreen(new MinLauncherTitleScreen());
        } else {
            minecraft.setScreen(null);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
