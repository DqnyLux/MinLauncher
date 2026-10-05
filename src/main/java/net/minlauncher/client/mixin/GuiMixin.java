package net.minlauncher.client.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.HudModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * HUD del cliente: inyección en el HUD de Minecraft para renderizar exclusivamente
 * los módulos HUD activos y legales de MinLauncher (FPS, CPS, Keystrokes, Coords, Armor, etc.).
 * Cero listas estilo cheat / arraylist.
 */
@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void minlauncher$renderModules(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (MinLauncher.getInstance() == null || MinLauncher.getInstance().getModuleManager() == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;

        float delta = deltaTracker.getGameTimeDeltaPartialTick(true);

        // Módulos HUD del cliente (FPS, CPS, Keystrokes, Coords, Armor, etc.)
        List<HudModule> hudModules = MinLauncher.getInstance().getModuleManager().getHudModules();
        for (HudModule hm : hudModules) {
            if (hm.isEnabled()) {
                try {
                    hm.render(extractor, delta);
                } catch (Exception e) {
                    MinLauncher.LOGGER.error("Error renderizando el módulo HUD {}", hm.getName(), e);
                }
            }
        }
    }
}
