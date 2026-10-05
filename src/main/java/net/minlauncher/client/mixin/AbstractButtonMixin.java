package net.minlauncher.client.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minlauncher.client.ui.render.RenderUtils;
import net.minlauncher.client.ui.theme.MLTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Moderniza y estiliza todos los botones estándar de Minecraft (Opciones, Pausa, etc.)
 * con el diseño oscuro de cristal acrílico y bordes dorados característico de MinLauncher.
 */
@Mixin(AbstractButton.class)
public abstract class AbstractButtonMixin extends AbstractWidget {

    public AbstractButtonMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Inject(method = "extractDefaultSprite", at = @At("HEAD"), cancellable = true)
    private void minlauncher$customButtonRender(GuiGraphicsExtractor extractor, CallbackInfo ci) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        int radius = Math.min(4, h / 4);

        if (!this.active) {
            // Estado Deshabilitado
            RenderUtils.drawRoundedRect(extractor, x, y, w, h, radius, 0x550F172A);
            RenderUtils.drawRoundedOutline(extractor, x, y, w, h, radius, 1, 0x33334155);
        } else if (this.isHoveredOrFocused()) {
            // Estado Hover / Enfocado con resplandor dorado
            RenderUtils.drawShadow(extractor, x, y, w, h, 3, MLTheme.GOLD_GLOW);
            RenderUtils.drawGradientV(extractor, x, y, w, h, 0xEE1E293B, 0xEE2A3B53);
            RenderUtils.drawRoundedOutline(extractor, x, y, w, h, radius, 1, MLTheme.BORDER_GOLD);
        } else {
            // Estado Normal moderno (frosted glass)
            RenderUtils.drawGradientV(extractor, x, y, w, h, 0xDD0D1117, 0xEE161B26);
            RenderUtils.drawRoundedOutline(extractor, x, y, w, h, radius, 1, MLTheme.BORDER_DEFAULT);
        }

        ci.cancel();
    }
}
