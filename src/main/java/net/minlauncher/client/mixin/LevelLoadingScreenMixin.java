package net.minlauncher.client.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Pantalla de carga del mundo (Singleplayer / Carga de chunks) personalizada:
 * Elimina la pantalla negra cuadrada y muestra un panel estético con gradiente suave
 * y barra de estado de generación del mundo con tonos dorados.
 */
@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin extends Screen {

    protected LevelLoadingScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void minlauncher$customLevelBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Fondo moderno oscuro espacial
        RenderUtils.drawGradientV(g, 0, 0, width, height, MLTheme.BG_DARK, MLTheme.BG_DARKEST);
        g.fillGradient(0, 0, width, 120, 0x18F59E0B, 0x00F59E0B);
        g.fillGradient(0, height - 100, width, height, 0x00FBBF24, 0x14FBBF24);

        // Header decorativo
        int logoAccent = MLTheme.getSmoothAccent();
        String titleText = "✦ MinLauncher · Cargando Mundo ✦";
        int tw = font.width(titleText);
        RenderUtils.drawPill(g, font, titleText, (width - tw - 24) / 2, 20, 0xAA0F172A, MLTheme.BORDER_DEFAULT, logoAccent);

        ci.cancel();
    }
}
