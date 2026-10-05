package net.minlauncher.client.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minlauncher.client.ui.theme.MLTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Título del cliente en la barra del sistema: se fuerza "MinLauncher - 1.0.0"
 * tanto al crear la ventana como en cada llamada a {@code setTitle}, porque
 * Minecraft vuelve a escribir su título por defecto al conectar o recargar.
 */
@Mixin(Window.class)
public class WindowTitleMixin {

    private static final String CLIENT_TITLE = MLTheme.NAME + " - " + MLTheme.VERSION;

    @ModifyVariable(method = "createWindow", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String minlauncher$windowTitle(String title) {
        return CLIENT_TITLE;
    }

    @ModifyVariable(method = "setTitle", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String minlauncher$keepTitle(String title) {
        return CLIENT_TITLE;
    }
}