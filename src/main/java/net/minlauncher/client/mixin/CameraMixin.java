package net.minlauncher.client.mixin;

import net.minecraft.client.Camera;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.module.impl.survival.ZoomModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "calculateFov", at = @At("RETURN"), cancellable = true)
    private void minlauncher$smoothZoomFov(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (MinLauncher.getInstance() == null || MinLauncher.getInstance().getModuleManager() == null) return;

        ZoomModule zoomModule = MinLauncher.getInstance().getModuleManager().getModule(ZoomModule.class);
        if (zoomModule == null) return;

        zoomModule.updateProgress(partialTicks);
        float progress = zoomModule.getProgress();

        if (progress > 0.001f) {
            float baseFov = cir.getReturnValue();
            float factor = zoomModule.getZoomFactor().getFloatValue();
            float targetFov = baseFov / factor;
            float interpolatedFov = baseFov + (targetFov - baseFov) * progress;
            cir.setReturnValue(interpolatedFov);
        }
    }
}
