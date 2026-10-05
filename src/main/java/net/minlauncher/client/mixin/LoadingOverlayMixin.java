package net.minlauncher.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Pantalla de carga inicial moderna personalizada estilo MinLauncher / MinEcuador:
 * Sustituye la pantalla roja/blanca de Mojang por un diseño espacial profundo,
 * logo dinámico dorado con resplandor, barra de carga redondeada con acento amarillo/oro
 * y transiciones suaves.
 */
@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private ReloadInstance reload;
    @Shadow private float currentProgress;
    @Shadow private long fadeOutStart;

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void minlauncher$renderCustomLoading(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int w = g.guiWidth();
        int h = g.guiHeight();

        // 1. Fondo espacial oscuro y degradados dorados suaves
        RenderUtils.drawGradientV(g, 0, 0, w, h, MLTheme.BG_DARK, MLTheme.BG_DARKEST);
        g.fillGradient(0, 0, w, 140, 0x22F59E0B, 0x00F59E0B);
        g.fillGradient(0, h - 120, w, h, 0x00FBBF24, 0x18FBBF24);

        int centerX = w / 2;
        int centerY = h / 2;

        // 2. Logo Hero Centrado "MinEcuador"
        int dynamicAccent = MLTheme.getSmoothAccent();
        String part1 = "Min";
        String part2 = "Ecuador";
        int w1 = this.minecraft.font.width(part1);
        int w2 = this.minecraft.font.width(part2);
        int totalLogoW = w1 + w2;

        g.pose().pushMatrix();
        g.pose().translate(centerX, centerY - 28);
        float logoScale = h < 300 ? 1.8f : 2.4f;
        g.pose().scale(logoScale, logoScale);
        RenderUtils.textShadowed(g, this.minecraft.font, part1, -totalLogoW / 2, -6, dynamicAccent);
        RenderUtils.textShadowed(g, this.minecraft.font, part2, -totalLogoW / 2 + w1, -6, MLTheme.WHITE);
        g.pose().popMatrix();

        // Subtítulo con píldora moderna
        String sub = "✦ CLIENTE DE ALTO RENDIMIENTO · CARGANDO RECURSOS ✦";
        int subW = this.minecraft.font.width(sub);
        int subX = centerX - (subW + 16) / 2;
        int subY = centerY + 4;
        RenderUtils.drawRoundedRect(g, subX, subY, subW + 16, 13, 6, 0x990F172A);
        RenderUtils.drawRoundedOutline(g, subX, subY, subW + 16, 13, 6, 1, MLTheme.BORDER_SUBTLE);
        g.text(this.minecraft.font, sub, subX + 8, subY + 3, MLTheme.GOLD_BRIGHT, false);

        // 3. Barra de Progreso Moderna Dorada
        float actualProgress = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.92f + actualProgress * 0.08f, 0.0f, 1.0f);

        int barW = Math.min(320, w - 80);
        int barH = 8;
        int barX = centerX - barW / 2;
        int barY = centerY + 28;

        // Fondo y resplandor de la barra
        RenderUtils.drawShadow(g, barX, barY, barW, barH, 4, MLTheme.SHADOW_DARK);
        RenderUtils.drawRoundedRect(g, barX, barY, barW, barH, 4, 0xCC0F172A);
        RenderUtils.drawRoundedOutline(g, barX, barY, barW, barH, 4, 1, MLTheme.BORDER_DEFAULT);

        int fillW = (int) (barW * this.currentProgress);
        if (fillW > 3) {
            RenderUtils.drawGradientH(g, barX + 1, barY + 1, fillW - 2, barH - 2, MLTheme.ACCENT, MLTheme.ACCENT_BRIGHT);
            RenderUtils.drawGlow(g, barX + 1, barY + 1, fillW - 2, barH - 2, 3, 1, MLTheme.ACCENT_GLOW);
        }

        // Porcentaje
        String pctStr = (int) (this.currentProgress * 100) + "%";
        int pctW = this.minecraft.font.width(pctStr);
        g.text(this.minecraft.font, pctStr, centerX - pctW / 2, barY + 12, MLTheme.TEXT_SECONDARY, false);

        // 4. Pie de pantalla
        String footer = "MinLauncher v" + MLTheme.VERSION + " · Optimizando shaders y texturas";
        int footW = this.minecraft.font.width(footer);
        g.text(this.minecraft.font, footer, centerX - footW / 2, h - 16, MLTheme.TEXT_MUTED, false);

        // Verificación de desvanecimiento / finalización
        if (this.reload.isDone() && this.fadeOutStart == -1L) {
            this.fadeOutStart = System.currentTimeMillis();
        }

        if (this.fadeOutStart > 0 && System.currentTimeMillis() - this.fadeOutStart > 600L) {
            this.minecraft.setOverlay(null);
        }

        ci.cancel();
    }
}
