package net.minlauncher.client.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

import java.util.List;

public class HudEditorScreen extends Screen {
    private HudModule draggingModule = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudEditorScreen() {
        super(Text.literal("Editor de HUD"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Cuadrícula sutil de alineación
        int gridSize = 20;
        for (int x = 0; x < width; x += gridSize) {
            RenderUtils.drawRect(context, x, 0, 1, height, 0x1AFFFFFF);
        }
        for (int y = 0; y < height; y += gridSize) {
            RenderUtils.drawRect(context, 0, y, width, 1, 0x1AFFFFFF);
        }

        // Barra informativa superior
        RenderUtils.drawRoundedRect(context, (width - 240) / 2, 10, 240, 26, 4, 0xCC18181B);
        context.drawText(textRenderer, "§bMinLauncher §f- Arrastra los elementos del HUD", (width - 240) / 2 + 10, 19, 0xFFFFFFFF, false);

        // Renderizar y bordear los módulos HUD
        List<HudModule> hudModules = MinLauncher.getInstance().getModuleManager().getHudModules();
        for (HudModule hm : hudModules) {
            if (hm.isEnabled()) {
                hm.renderDummy(context, delta);

                // Dibujar caja de selección
                boolean hovered = hm.isHovered(mouseX, mouseY);
                int borderColor = (hm == draggingModule || hovered) ? 0xFF3B82F6 : 0x663F3F46;
                RenderUtils.drawBorder(context, hm.getX() - 1, hm.getY() - 1, hm.getWidth() + 2, hm.getHeight() + 2, 1, borderColor);
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
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
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingModule != null) {
            // Magnetic snap a la cuadrícula de 5px
            int snappedX = Math.round((float) draggingModule.getX() / 5) * 5;
            int snappedY = Math.round((float) draggingModule.getY() / 5) * 5;
            draggingModule.setX(snappedX);
            draggingModule.setY(snappedY);

            draggingModule = null;
            MinLauncher.getInstance().getConfigManager().saveConfig();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingModule != null) {
            draggingModule.setX((int) mouseX - dragOffsetX);
            draggingModule.setY((int) mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
