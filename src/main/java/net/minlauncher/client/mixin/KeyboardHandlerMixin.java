package net.minlauncher.client.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.ModuleManager;
import net.minlauncher.client.ui.ClickGuiScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Detección de teclado de MinLauncher.
 *
 * NOTA VERIFICADA (bytecode de 26.x): {@code KeyboardHandler.keyPress(long, int, KeyEvent)}
 * recibe el 2º int como ACCIÓN GLFW (1=PRESS, 2=REPEAT, 0=RELEASE), NO el código de tecla;
 * y {@code KeyEvent.input()} devuelve el código GLFW de la tecla (ej. 344=RSHIFT), NO la acción.
 * Por eso se usa {@code event.key()} para la tecla y se filtra la acción con este 2º int.
 */
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    private static final Map<Integer, Long> LAST_KEY_PRESS = new HashMap<>();
    private static final long DEBOUNCE_MS = 100;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void minlauncher$onKeyInput(long window, int action, KeyEvent event, CallbackInfo ci) {
        if (event == null) return;

        int key = event.key();
        if (MinLauncher.getInstance() == null) return;
        ModuleManager moduleManager = MinLauncher.getInstance().getModuleManager();

        // 1. Manejo prioritario de RSHIFT (Abrir / Cerrar ClickGUI de módulos)
        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT && action == GLFW.GLFW_PRESS) {
            long now = System.currentTimeMillis();
            Long last = LAST_KEY_PRESS.get(key);
            if (last == null || now - last > DEBOUNCE_MS) {
                LAST_KEY_PRESS.put(key, now);
                if (minecraft.screen instanceof ClickGuiScreen) {
                    minecraft.execute(() -> minecraft.setScreen(null));
                } else if (minecraft.screen == null) {
                    minecraft.execute(() -> minecraft.setScreen(new ClickGuiScreen()));
                }
            }
            ci.cancel();
            return;
        }

        // Si hay una pantalla abierta (chat, inventario, opciones, etc.), no interceptar teclas de módulos
        if (minecraft.screen != null) return;

        // RELEASE: para módulos tipo "hold" (Zoom al soltar C)
        if (action == GLFW.GLFW_RELEASE) {
            moduleManager.onKeyUp(key);
            return;
        }

        if (action != GLFW.GLFW_PRESS) return; // Ignora REPEAT duplicados

        long now = System.currentTimeMillis();
        Long last = LAST_KEY_PRESS.get(key);
        if (last != null && now - last < DEBOUNCE_MS) return;
        LAST_KEY_PRESS.put(key, now);

        // Teclas de módulos (C = Zoom, V = ToggleSprint, B = Fullbright, etc.)
        moduleManager.onKeyDown(key);
    }
}
