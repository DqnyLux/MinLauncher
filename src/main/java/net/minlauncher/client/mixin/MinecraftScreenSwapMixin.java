package net.minlauncher.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.ui.screen.MinLauncherPauseScreen;
import net.minlauncher.client.ui.screen.MinLauncherTitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Reemplaza las pantallas vanilla por las de MinLauncher: TODO screen que el
 * juego intente mostrar como {TitleScreen} o {PauseScreen} se sustituye por la
 * versión del cliente. Se cubren los dos puntos de entrada de Minecraft 26.x:
 * {setScreen} (pausa, desconexión) y {setScreenAndShow} (boot del juego).
 */
@Mixin(Minecraft.class)
public class MinecraftScreenSwapMixin {

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Screen minlauncher$swapViaSetScreen(Screen value) {
        return swap(value);
    }

    @ModifyVariable(method = "setScreenAndShow", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Screen minlauncher$swapViaSetScreenAndShow(Screen value) {
        return swap(value);
    }

    private Screen swap(Screen value) {
        if (value instanceof TitleScreen && !(value instanceof MinLauncherTitleScreen)) {
            MinLauncher.LOGGER.info("Pantalla reemplazada: TitleScreen -> MinLauncherTitleScreen");
            return new MinLauncherTitleScreen();
        }
        if (value instanceof PauseScreen && !(value instanceof MinLauncherPauseScreen)) {
            MinLauncher.LOGGER.info("Pantalla reemplazada: PauseScreen -> MinLauncherPauseScreen");
            return new MinLauncherPauseScreen();
        }
        return value;
    }
}