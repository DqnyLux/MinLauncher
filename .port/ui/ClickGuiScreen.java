package net.minlauncher.client.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ModeSetting;
import net.minlauncher.client.module.setting.NumberSetting;
import net.minlauncher.client.module.setting.Setting;
import net.minlauncher.client.ui.render.RenderUtils;

import java.util.List;

public class ClickGuiScreen extends Screen {
    private Category selectedCategory = Category.PVP;
    private Module expandedModule = null;

    private int guiX = 60;
    private int guiY = 40;
    private int guiWidth = 400;
    private int guiHeight = 260;

    public ClickGuiScreen() {
        super(Text.literal("MinLauncher Client Menu"));
    }

    @Override
    protected void init() {
        guiX = (this.width - guiWidth) / 2;
        guiY = (this.height - guiHeight) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Fondo translúcido con desenfoque simulado
        RenderUtils.drawRect(context, 0, 0, width, height, 0x66000000);

        // Ventana Principal
        RenderUtils.drawRoundedRect(context, guiX, guiY, guiWidth, guiHeight, 6, 0xEE18181B);
        RenderUtils.drawBorder(context, guiX, guiY, guiWidth, guiHeight, 1, 0xFF3F3F46);

        // Barra Superior / Header
        RenderUtils.drawRoundedRect(context, guiX, guiY, guiWidth, 30, 6, 0xFF27272A);
        context.drawText(textRenderer, MinLauncher.CLIENT_NAME + " §7v" + MinLauncher.CLIENT_VERSION, guiX + 12, guiY + 11, 0xFFFFFFFF, true);

        // Botón directo a HUD Editor y Cosméticos
        int btnHudX = guiX + guiWidth - 160;
        int btnCosmeticsX = guiX + guiWidth - 85;

        RenderUtils.drawRect(context, btnHudX, guiY + 6, 70, 18, 0xFF3B82F6);
        context.drawText(textRenderer, "Editar HUD", btnHudX + 8, guiY + 11, 0xFFFFFFFF, false);

        RenderUtils.drawRect(context, btnCosmeticsX, guiY + 6, 75, 18, 0xFF8B5CF6);
        context.drawText(textRenderer, "Cosméticos", btnCosmeticsX + 7, guiY + 11, 0xFFFFFFFF, false);

        // Panel Izquierdo (Categorías)
        int catY = guiY + 40;
        int catWidth = 100;
        for (Category cat : Category.values()) {
            boolean isSelected = cat == selectedCategory;
            int bgColor = isSelected ? 0xFF3B82F6 : 0x00000000;
            int textColor = isSelected ? 0xFFFFFFFF : 0xFFA1A1AA;

            if (isSelected) {
                RenderUtils.drawRoundedRect(context, guiX + 6, catY, catWidth - 12, 22, 4, bgColor);
            }
            context.drawText(textRenderer, cat.getDisplayName(), guiX + 14, catY + 7, textColor, false);
            catY += 26;
        }

        // Separador vertical
        RenderUtils.drawRect(context, guiX + catWidth, guiY + 30, 1, guiHeight - 30, 0xFF3F3F46);

        // Panel Derecho (Lista de Módulos)
        int modX = guiX + catWidth + 10;
        int modY = guiY + 40;
        int modWidth = guiWidth - catWidth - 20;

        List<Module> modules = MinLauncher.getInstance().getModuleManager().getModulesByCategory(selectedCategory);
        for (Module m : modules) {
            int modBg = m.isEnabled() ? 0xFF2563EB : 0xFF27272A;
            RenderUtils.drawRoundedRect(context, modX, modY, modWidth, 24, 4, modBg);

            // Nombre y Estado
            context.drawText(textRenderer, m.getName(), modX + 8, modY + 8, 0xFFFFFFFF, true);
            String status = m.isEnabled() ? "§aON" : "§cOFF";
            context.drawText(textRenderer, status, modX + modWidth - 30, modY + 8, 0xFFFFFFFF, false);

            modY += 28;

            // Si está expandido mostrar settings
            if (m == expandedModule) {
                for (Setting<?> s : m.getSettings()) {
                    RenderUtils.drawRect(context, modX + 10, modY, modWidth - 20, 18, 0xFF18181B);
                    context.drawText(textRenderer, "• " + s.getName(), modX + 14, modY + 5, 0xFFA1A1AA, false);

                    if (s instanceof BooleanSetting bs) {
                        String bVal = bs.getValue() ? "§aHabilitado" : "§cDeshabilitado";
                        context.drawText(textRenderer, bVal, modX + modWidth - 90, modY + 5, 0xFFFFFFFF, false);
                    } else if (s instanceof NumberSetting ns) {
                        context.drawText(textRenderer, String.valueOf(ns.getValue()), modX + modWidth - 50, modY + 5, 0xFFFFFFFF, false);
                    } else if (s instanceof ModeSetting ms) {
                        context.drawText(textRenderer, ms.getValue(), modX + modWidth - 60, modY + 5, 0xFFE4E4E7, false);
                    }
                    modY += 20;
                }
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int btnHudX = guiX + guiWidth - 160;
        int btnCosmeticsX = guiX + guiWidth - 85;

        // Click en Editar HUD
        if (mouseX >= btnHudX && mouseX <= btnHudX + 70 && mouseY >= guiY + 6 && mouseY <= guiY + 24) {
            if (client != null) client.setScreen(new HudEditorScreen());
            return true;
        }

        // Click en Cosméticos
        if (mouseX >= btnCosmeticsX && mouseX <= btnCosmeticsX + 75 && mouseY >= guiY + 6 && mouseY <= guiY + 24) {
            if (client != null) client.setScreen(new CosmeticsScreen());
            return true;
        }

        // Selección de Categorías
        int catY = guiY + 40;
        int catWidth = 100;
        for (Category cat : Category.values()) {
            if (mouseX >= guiX + 6 && mouseX <= guiX + catWidth - 6 && mouseY >= catY && mouseY <= catY + 22) {
                selectedCategory = cat;
                expandedModule = null;
                return true;
            }
            catY += 26;
        }

        // Clics en Módulos
        int modX = guiX + catWidth + 10;
        int modY = guiY + 40;
        int modWidth = guiWidth - catWidth - 20;

        List<Module> modules = MinLauncher.getInstance().getModuleManager().getModulesByCategory(selectedCategory);
        for (Module m : modules) {
            if (mouseX >= modX && mouseX <= modX + modWidth && mouseY >= modY && mouseY <= modY + 24) {
                if (button == 0) { // Click Izquierdo -> Toggle
                    m.toggle();
                } else if (button == 1) { // Click Derecho -> Expandir Settings
                    expandedModule = (expandedModule == m) ? null : m;
                }
                return true;
            }
            modY += 28;

            if (m == expandedModule) {
                for (Setting<?> s : m.getSettings()) {
                    if (mouseX >= modX + 10 && mouseX <= modX + modWidth - 10 && mouseY >= modY && mouseY <= modY + 18) {
                        if (s instanceof BooleanSetting bs) {
                            bs.toggle();
                        } else if (s instanceof ModeSetting ms) {
                            ms.cycle();
                        }
                        return true;
                    }
                    modY += 20;
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
