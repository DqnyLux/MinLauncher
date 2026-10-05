package net.minlauncher.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;

import java.util.List;

/**
 * Editor visual de HUD de MinLauncher estilo Lunar Client: arrastre de elementos
 * con ajuste magnético a la cuadrícula.
 */
public class HudEditorScreen extends Screen {
    private HudModule draggingModule = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudEditorScreen() {
        super(Component.literal("Editor de HUD"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        // Cuadrícula sutil de alineación dorada
        int gridSize = 20;
        for (int x = 0; x < width; x += gridSize) {
            RenderUtils.drawRect(extractor, x, 0, 1, height, 0x12F59E0B);
        }
        for (int y = 0; y < height; y += gridSize) {
            RenderUtils.drawRect(extractor, 0, y, width, 1, 0x12F59E0B);
        }

        // Barra informativa superior
        String title = "MinLauncher · Arrastra los elementos del HUD a la posición deseada";
        int tw = font.width(title);
        RenderUtils.drawPill(extractor, font, title, (width - tw - 20) / 2, 10, 0xDD0B1120, MLTheme.BORDER_DEFAULT, MLTheme.GOLD);

        // Renderizar y bordear los módulos HUD
        List<HudModule> hudModules = MinLauncher.getInstance().getModuleManager().getHudModules();
        for (HudModule hm : hudModules) {
            if (hm.isEnabled()) {
                hm.renderDummy(extractor, delta);

                // Dibujar caja de selección interactiva
                boolean hovered = hm.isHovered(mouseX, mouseY);
                boolean isDragging = (hm == draggingModule);
                int borderColor = isDragging ? MLTheme.GOLD_BRIGHT : (hovered ? MLTheme.ACCENT_BRIGHT : MLTheme.BORDER_DEFAULT);

                RenderUtils.drawRoundedOutline(extractor, hm.getX() - 2, hm.getY() - 2, hm.getWidth() + 4, hm.getHeight() + 4, 3, 1, borderColor);
                if (isDragging || hovered) {
                    RenderUtils.drawGlow(extractor, hm.getX() - 2, hm.getY() - 2, hm.getWidth() + 4, hm.getHeight() + 4, 3, 1, MLTheme.GOLD_GLOW);
                }
            }
        }

        super.extractRenderState(extractor, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean captured) {
        double mouseX = event.x();
        double mouseY = event.y();
        if (event.button() == 0) {
            List<HudModule> hudModules = MinLauncher.getInstance().getModuleManager().getHudModules();
            for (HudModule hm : hudModules) {
                if (hm.isEnabled() && hm.isHovered(mouseX, mouseY)) {
                    draggingModule = hm;
                    dragOffsetX = (int) mouseX - hm.getX();
                    dragOffsetY = (int) mouseY - hm.getY();
                    return true;
                }
            }
        }
        return super.mouseClicked(event, captured);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0 && draggingModule != null) {
            // Ajuste magnético a la cuadrícula de 5px
            int snappedX = Math.round((float) draggingModule.getX() / 5) * 5;
            int snappedY = Math.round((float) draggingModule.getY() / 5) * 5;
            draggingModule.setX(snappedX);
            draggingModule.setY(snappedY);

            draggingModule = null;
            MinLauncher.getInstance().getConfigManager().saveConfig();
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (draggingModule != null) {
            draggingModule.setX((int) event.x() - dragOffsetX);
            draggingModule.setY((int) event.y() - dragOffsetY);
            return true;
        }
        return super.mouseDragged(event, deltaX, deltaY);
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