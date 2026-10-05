package net.minlauncher.client.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.cosmetics.Cosmetic;
import net.minlauncher.client.cosmetics.CosmeticType;
import net.minlauncher.client.cosmetics.badge.Badge;
import net.minlauncher.client.ui.render.RenderUtils;

import java.util.List;

public class CosmeticsScreen extends Screen {
    private CosmeticType selectedType = CosmeticType.CAPE;
    private float playerRotation = 0f;

    public CosmeticsScreen() {
        super(Text.literal("Tienda y Armario de Cosméticos"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.drawRect(context, 0, 0, width, height, 0xAA09090B);

        int panelX = (width - 460) / 2;
        int panelY = (height - 280) / 2;
        int panelW = 460;
        int panelH = 280;

        // Ventana Principal
        RenderUtils.drawRoundedRect(context, panelX, panelY, panelW, panelH, 6, 0xEE18181B);
        RenderUtils.drawBorder(context, panelX, panelY, panelW, panelH, 1, 0xFF8B5CF6);

        // Header
        RenderUtils.drawRoundedRect(context, panelX, panelY, panelW, 30, 6, 0xFF27272A);
        context.drawText(textRenderer, "✦ Armario de Cosméticos & Insignias", panelX + 12, panelY + 11, 0xFFFFFFFF, true);

        // Renderizado del jugador en 3D
        int playerPreviewX = panelX + 75;
        int playerPreviewY = panelY + 220;
        RenderUtils.drawRoundedRect(context, panelX + 10, panelY + 40, 130, 230, 4, 0xFF09090B);
        RenderUtils.drawBorder(context, panelX + 10, panelY + 40, 130, 230, 1, 0xFF27272A);

        if (client != null && client.player != null) {
            playerRotation += 0.8f;
            InventoryScreen.drawEntity(context, playerPreviewX - 35, playerPreviewY - 140, playerPreviewX + 35, playerPreviewY, 55, 0.0625f, (float) Math.sin(Math.toRadians(playerRotation)) * 20, 0, client.player);
        }

        // Selector de Pestañas de Cosméticos
        int tabX = panelX + 150;
        int tabY = panelY + 40;
        for (CosmeticType type : CosmeticType.values()) {
            boolean isSel = type == selectedType;
            int btnBg = isSel ? 0xFF8B5CF6 : 0xFF27272A;
            RenderUtils.drawRoundedRect(context, tabX, tabY, 58, 20, 3, btnBg);
            context.drawText(textRenderer, type.getDisplayName(), tabX + 4, tabY + 6, 0xFFFFFFFF, false);
            tabX += 62;
        }

        // Lista de Cosméticos de la Categoría
        int cardX = panelX + 150;
        int cardY = panelY + 70;
        int cardW = 145;
        int cardH = 40;

        if (selectedType == CosmeticType.BADGE) {
            // Mostrar Insignias
            for (Badge badge : MinLauncher.getInstance().getBadgeManager().getRegisteredBadges().values()) {
                RenderUtils.drawRoundedRect(context, cardX, cardY, cardW, cardH, 4, 0xFF27272A);
                context.drawText(textRenderer, badge.getPrefixText() + " " + badge.getName(), cardX + 8, cardY + 8, badge.getColorHex(), true);
                context.drawText(textRenderer, "§aInsignia Activa", cardX + 8, cardY + 24, 0xFFA1A1AA, false);

                cardX += cardW + 8;
                if (cardX + cardW > panelX + panelW - 10) {
                    cardX = panelX + 150;
                    cardY += cardH + 8;
                }
            }
        } else {
            // Mostrar Capas, Alas, etc.
            List<Cosmetic> list = MinLauncher.getInstance().getCosmeticsManager().getCosmeticsByType(selectedType);
            for (Cosmetic cos : list) {
                int cardBg = cos.isEquipped() ? 0xFF1E3A8A : 0xFF27272A;
                RenderUtils.drawRoundedRect(context, cardX, cardY, cardW, cardH, 4, cardBg);
                RenderUtils.drawBorder(context, cardX, cardY, cardW, cardH, 1, cos.isEquipped() ? 0xFF3B82F6 : 0xFF3F3F46);

                context.drawText(textRenderer, cos.getName(), cardX + 8, cardY + 8, 0xFFFFFFFF, true);
                String eqText = cos.isEquipped() ? "§a✓ Equipado" : "§7Clic para equipar";
                context.drawText(textRenderer, eqText, cardX + 8, cardY + 24, 0xFFFFFFFF, false);

                cardX += cardW + 8;
                if (cardX + cardW > panelX + panelW - 10) {
                    cardX = panelX + 150;
                    cardY += cardH + 8;
                }
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelX = (width - 460) / 2;
        int panelY = (height - 280) / 2;
        int panelW = 460;

        // Clic en Pestañas
        int tabX = panelX + 150;
        int tabY = panelY + 40;
        for (CosmeticType type : CosmeticType.values()) {
            if (mouseX >= tabX && mouseX <= tabX + 58 && mouseY >= tabY && mouseY <= tabY + 20) {
                selectedType = type;
                return true;
            }
            tabX += 62;
        }

        // Clic en Equipar/Desequipar Cosméticos
        if (selectedType != CosmeticType.BADGE) {
            int cardX = panelX + 150;
            int cardY = panelY + 70;
            int cardW = 145;
            int cardH = 40;

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

                cardX += cardW + 8;
                if (cardX + cardW > panelX + panelW - 10) {
                    cardX = panelX + 150;
                    cardY += cardH + 8;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
