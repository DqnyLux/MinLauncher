package net.minlauncher.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.HudModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (MinLauncher.getInstance() == null || MinLauncher.getInstance().getModuleManager() == null) return;

        List<HudModule> hudModules = MinLauncher.getInstance().getModuleManager().getHudModules();
        float delta = tickCounter.getTickDelta(true);

        for (HudModule hm : hudModules) {
            if (hm.isEnabled()) {
                hm.render(context, delta);
            }
        }
    }
}
