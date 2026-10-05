package net.minlauncher.client.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.ui.ClickGuiScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKeyInput(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == GLFW.GLFW_PRESS) {
            // Abrir ClickGUI con RSHIFT por defecto
            if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                }
                return;
            }

            // Procesar keybinds de módulos
            if (client.currentScreen == null && MinLauncher.getInstance() != null) {
                MinLauncher.getInstance().getModuleManager().onKey(key);
            }
        }
    }
}
