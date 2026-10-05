package net.minlauncher.client.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Pantalla de conexión a servidores personalizada estilo Lunar/Badlion Client:
 * Elimina la pantalla vacía con texto básico y la sustituye por una tarjeta frosted glass
 * central con indicador pulsante y estado estilizado en tonos dorados.
 */
@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {

    @Shadow private Component status;

    protected ConnectScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void minlauncher$renderCustomConnect(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Fondo translúcido profundo
        RenderUtils.drawGradientV(g, 0, 0, width, height, MLTheme.BG_DARK, MLTheme.BG_DARKEST);
        g.fillGradient(0, 0, width, 100, 0x18F59E0B, 0x00F59E0B);

        int panelW = Math.min(360, width - 40);
        int panelH = 130;
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2 - 10;

        // Tarjeta central frosted glass
        RenderUtils.drawShadow(g, panelX, panelY, panelW, panelH, 6, MLTheme.SHADOW_DARK);
        RenderUtils.drawRoundedRect(g, panelX, panelY, panelW, panelH, 8, MLTheme.BG_PANEL);
        RenderUtils.drawRoundedOutline(g, panelX, panelY, panelW, panelH, 8, 1, MLTheme.BORDER_DEFAULT);

        // Cabecera de la tarjeta
        RenderUtils.drawRoundedRect(g, panelX + 1, panelY + 1, panelW - 2, 26, 7, 0xAA0F172A);
        RenderUtils.drawGradientH(g, panelX + 6, panelY + 26, panelW - 12, 1, MLTheme.ACCENT, MLTheme.GOLD);
        g.text(font, "✦ Conectando al servidor", panelX + 12, panelY + 9, MLTheme.WHITE, true);

        // Indicador de conexión / Pulso dinámico dorado
        int dotPulse = MLTheme.getSmoothAccent();
        int iconX = panelX + panelW / 2;
        int iconY = panelY + 48;

        g.text(font, "🌐", iconX - 6, iconY, dotPulse, false);

        // Texto del estado de conexión
        if (this.status != null) {
            int sw = font.width(this.status);
            g.text(font, this.status, (width - sw) / 2, panelY + 70, MLTheme.TEXT_PRIMARY, false);
        }

        // Subtítulo con tag de MinLauncher
        String tag = "MinLauncher Network Engine · Latencia optimizada";
        int tw = font.width(tag);
        g.text(font, tag, (width - tw) / 2, panelY + 92, MLTheme.TEXT_MUTED, false);

        // Renderizar botones (como el botón Cancelar)
        super.extractRenderState(g, mouseX, mouseY, delta);

        ci.cancel();
    }
}
