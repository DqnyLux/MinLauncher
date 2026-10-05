package net.minlauncher.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.cosmetics.Cosmetic;
import net.minlauncher.client.cosmetics.CosmeticType;
import net.minlauncher.client.cosmetics.badge.Badge;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;

import java.util.List;

/**
 * Pantalla de Armario y Cosméticos de MinLauncher: estilo Lunar / Badlion con fondos transparentes.
 */
public class CosmeticsScreen extends Screen {
    private CosmeticType selectedType = CosmeticType.CAPE;

    public CosmeticsScreen() {
        super(Component.literal("Armario de Cosméticos"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        // Fondo translúcido general
        RenderUtils.drawGradientV(extractor, 0, 0, width, height, 0x60050811, 0x750B1120);

        int panelW = Math.min(420, width - 20);
        int panelH = Math.min(250, height - 24);
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        // Ventana Principal transparente con sombra y borde dorado
        RenderUtils.drawShadow(extractor, panelX, panelY, panelW, panelH, 6, MLTheme.SHADOW_DARK);
        RenderUtils.drawRoundedRect(extractor, panelX, panelY, panelW, panelH, 7, MLTheme.BG_PANEL);
        RenderUtils.drawRoundedOutline(extractor, panelX, panelY, panelW, panelH, 7, 1, MLTheme.BORDER_DEFAULT);

        // Header translúcido
        RenderUtils.drawRoundedRect(extractor, panelX + 1, panelY + 1, panelW - 2, 24, 6, 0x800E1017);
        RenderUtils.drawGradientH(extractor, panelX + 6, panelY + 24, panelW - 12, 1, MLTheme.ACCENT, MLTheme.GOLD);
        extractor.text(font, "✦ Armario de Cosméticos & Insignias", panelX + 10, panelY + 8, MLTheme.GOLD_BRIGHT, true);

        // Panel lateral de vista previa
        int previewW = 100;
        int previewH = panelH - 36;
        RenderUtils.drawRoundedRect(extractor, panelX + 8, panelY + 28, previewW, previewH, 5, 0x600B1120);
        RenderUtils.drawRoundedOutline(extractor, panelX + 8, panelY + 28, previewW, previewH, 5, 1, MLTheme.BORDER_SUBTLE);

        // Vista previa visual
        Cosmetic equipped = MinLauncher.getInstance().getCosmeticsManager().getEquipped(selectedType);
        String previewName = equipped != null ? equipped.getName() : "Ninguno";
        int pw = font.width("Vista previa");
        extractor.text(font, "Vista previa", panelX + 8 + (previewW - pw) / 2, panelY + 36, MLTheme.GOLD_BRIGHT, true);

        int iconY = panelY + previewH / 2;
        String icon = switch (selectedType) {
            case CAPE -> "⚑";
            case WINGS -> "🪽";
            case BANDANA -> "🧣";
            case HAT -> "👑";
            case BADGE -> "★";
        };
        int iw = font.width(icon);
        extractor.text(font, icon, panelX + 8 + (previewW - iw) / 2, iconY - 14, MLTheme.ACCENT_BRIGHT, true);

        int pnw = font.width(previewName);
        if (pnw > previewW - 10) {
            previewName = previewName.substring(0, Math.min(previewName.length(), 12)) + "..";
            pnw = font.width(previewName);
        }
        extractor.text(font, previewName, panelX + 8 + (previewW - pnw) / 2, iconY + 4, MLTheme.WHITE, false);

        // Selector de Pestañas de Cosméticos
        int contentX = panelX + previewW + 16;
        int contentW = panelW - previewW - 24;
        int tabY = panelY + 28;
        CosmeticType[] types = CosmeticType.values();
        int tabBtnW = (contentW - (types.length - 1) * 3) / types.length;
        int tabBtnH = 16;

        for (int i = 0; i < types.length; i++) {
            CosmeticType type = types[i];
            int tabX = contentX + i * (tabBtnW + 3);
            boolean isSel = type == selectedType;
            boolean isHover = mouseX >= tabX && mouseX <= tabX + tabBtnW && mouseY >= tabY && mouseY <= tabY + tabBtnH;

            int btnBg = isSel ? MLTheme.BG_FEATURED : (isHover ? MLTheme.BG_CARD_HOVER : MLTheme.BG_CARD);
            int btnBorder = isSel ? MLTheme.BORDER_GOLD : (isHover ? MLTheme.BORDER_HOVER : MLTheme.BORDER_SUBTLE);
            int textColor = isSel ? MLTheme.GOLD_BRIGHT : (isHover ? MLTheme.WHITE : MLTheme.TEXT_SECONDARY);

            RenderUtils.drawRoundedRect(extractor, tabX, tabY, tabBtnW, tabBtnH, 4, btnBg);
            RenderUtils.drawRoundedOutline(extractor, tabX, tabY, tabBtnW, tabBtnH, 4, 1, btnBorder);
            int tw = font.width(type.getDisplayName());
            extractor.text(font, type.getDisplayName(), tabX + (tabBtnW - tw) / 2, tabY + 4, textColor, isSel);
        }

        // Lista de Cosméticos de la Categoría
        int cardX = contentX;
        int cardY = panelY + 50;
        int cardW = (contentW - 6) / 2;
        int cardH = 34;

        if (selectedType == CosmeticType.BADGE) {
            // Mostrar Insignias
            for (Badge badge : MinLauncher.getInstance().getBadgeManager().getRegisteredBadges().values()) {
                boolean hover = mouseX >= cardX && mouseX <= cardX + cardW && mouseY >= cardY && mouseY <= cardY + cardH;
                int bg = hover ? MLTheme.BG_CARD_HOVER : MLTheme.BG_CARD;
                RenderUtils.drawRoundedRect(extractor, cardX, cardY, cardW, cardH, 4, bg);
                RenderUtils.drawRoundedOutline(extractor, cardX, cardY, cardW, cardH, 4, 1, hover ? MLTheme.BORDER_GOLD : MLTheme.BORDER_SUBTLE);

                extractor.text(font, badge.getPrefixText() + " " + badge.getName(), cardX + 6, cardY + 6, badge.getColorHex(), true);
                extractor.text(font, "Insignia Activa", cardX + 6, cardY + 18, MLTheme.TEXT_DIM, false);

                cardX += cardW + 6;
                if (cardX + cardW > panelX + panelW - 6) {
                    cardX = contentX;
                    cardY += cardH + 6;
                }
            }
        } else {
            // Mostrar Capas, Alas, etc.
            List<Cosmetic> list = MinLauncher.getInstance().getCosmeticsManager().getCosmeticsByType(selectedType);
            for (Cosmetic cos : list) {
                boolean hover = mouseX >= cardX && mouseX <= cardX + cardW && mouseY >= cardY && mouseY <= cardY + cardH;
                int cardBg = cos.isEquipped() ? MLTheme.BG_FEATURED : (hover ? MLTheme.BG_CARD_HOVER : MLTheme.BG_CARD);
                int cardBorder = cos.isEquipped() ? MLTheme.BORDER_GOLD : (hover ? MLTheme.BORDER_HOVER : MLTheme.BORDER_SUBTLE);

                RenderUtils.drawRoundedRect(extractor, cardX, cardY, cardW, cardH, 4, cardBg);
                RenderUtils.drawRoundedOutline(extractor, cardX, cardY, cardW, cardH, 4, 1, cardBorder);

                extractor.text(font, cos.getName(), cardX + 6, cardY + 6, MLTheme.WHITE, true);
                String eqText = cos.isEquipped() ? "✔ Equipado" : (hover ? "Clic para equipar" : "Disponible");
                int eqColor = cos.isEquipped() ? MLTheme.GOLD_BRIGHT : (hover ? MLTheme.WHITE : MLTheme.TEXT_MUTED);
                extractor.text(font, eqText, cardX + 6, cardY + 18, eqColor, false);

                cardX += cardW + 6;
                if (cardX + cardW > panelX + panelW - 6) {
                    cardX = contentX;
                    cardY += cardH + 6;
                }
            }
        }

        super.extractRenderState(extractor, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean captured) {
        double mouseX = event.x();
        double mouseY = event.y();
        int panelW = Math.min(420, width - 20);
        int panelH = Math.min(250, height - 24);
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;
        int previewW = 100;
        int contentX = panelX + previewW + 16;
        int contentW = panelW - previewW - 24;

        // Clic en Pestañas
        int tabY = panelY + 28;
        CosmeticType[] types = CosmeticType.values();
        int tabBtnW = (contentW - (types.length - 1) * 3) / types.length;
        int tabBtnH = 16;

        for (int i = 0; i < types.length; i++) {
            int tabX = contentX + i * (tabBtnW + 3);
            if (mouseX >= tabX && mouseX <= tabX + tabBtnW && mouseY >= tabY && mouseY <= tabY + tabBtnH) {
                selectedType = types[i];
                return true;
            }
        }

        // Clic en Equipar/Desequipar Cosméticos
        if (selectedType != CosmeticType.BADGE) {
            int cardX = contentX;
            int cardY = panelY + 50;
            int cardW = (contentW - 6) / 2;
            int cardH = 34;

            List<Cosmetic> list = MinLauncher.getInstance().getCosmeticsManager().getCosmeticsByType(selectedType);
            for (Cosmetic cos : list) {
                if (mouseX >= cardX && mouseX <= cardX + cardW && mouseY >= cardY && mouseY <= cardY + cardH) {
                    if (cos.isEquipped()) {
                        MinLauncher.getInstance().getCosmeticsManager().unequipCosmetic(cos);
                    } else {
                        MinLauncher.getInstance().getCosmeticsManager().equipCosmetic(cos);
                    }
                    MinLauncher.getInstance().getConfigManager().saveConfig();
                    return true;
                }

                cardX += cardW + 6;
                if (cardX + cardW > panelX + panelW - 6) {
                    cardX = contentX;
                    cardY += cardH + 6;
                }
            }
        }

        return super.mouseClicked(event, captured);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (minecraft.level == null) {
            minecraft.setScreen(new net.minlauncher.client.ui.screen.MinLauncherTitleScreen());
        } else {
            minecraft.setScreen(new ClickGuiScreen());
        }
    }
}
