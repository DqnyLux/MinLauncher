package net.minlauncher.client.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
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
 * Moderniza el fondo de todos los submenús de Minecraft (Opciones, Multijugador, Mundos, Estadísticas):
 * Reemplaza la textura repetitiva antigua de tierra/dirt por un fondo acrílico oscuro
 * profundo con degradado suave y barras separadoras modernas doradas/ámbar.
 */
@Mixin(Screen.class)
public class ScreenDecorationMixin {

    @Shadow public int width;
    @Shadow public int height;
    @Shadow protected Component title;

    @Inject(method = "extractMenuBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V", at = @At("HEAD"), cancellable = true)
    private void minlauncher$customMenuBackground(GuiGraphicsExtractor g, int x, int y, int width, int height, CallbackInfo ci) {
        // Fondo degradado moderno tipo frosted glass
        RenderUtils.drawGradientV(g, x, y, width, height, MLTheme.BG_DARK, MLTheme.BG_DARKEST);
        g.fillGradient(x, y, width, Math.min(height, 80), 0x18F59E0B, 0x00F59E0B);
        g.fillGradient(x, Math.max(0, height - 70), width, height, 0x00FBBF24, 0x12FBBF24);

        // Barra decorativa sutil en la cabecera
        RenderUtils.drawGradientH(g, x + 20, 32, width - 40, 1, MLTheme.ACCENT, MLTheme.GOLD);

        ci.cancel();
    }
}
