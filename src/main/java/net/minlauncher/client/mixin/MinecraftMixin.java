package net.minlauncher.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.Options;
import net.minlauncher.client.MinLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow public Options options;
    private static boolean minlauncher$defaultsApplied = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void minlauncher$onClientTick(CallbackInfo ci) {
        if (!minlauncher$defaultsApplied) {
            minlauncher$defaultsApplied = true;
            if (options != null) {
                // Desactivar narrador por defecto
                try {
                    options.narrator().set(NarratorStatus.OFF);
                } catch (Exception ignored) {}

                // Configurar guiScale en 2 por defecto si está en Auto (0)
                try {
                    if (options.guiScale().get() == 0) {
                        options.guiScale().set(2);
                    }
                } catch (Exception ignored) {}

                // Idioma predeterminado: Español (Latinoamérica) es_mx
                try {
                    if (options.languageCode == null || options.languageCode.equals("en_us") || options.languageCode.isEmpty()) {
                        options.languageCode = "es_mx";
                        Minecraft.getInstance().getLanguageManager().setSelected("es_mx");
                    }
                } catch (Exception ignored) {}
            }
        }

        if (MinLauncher.getInstance() != null && MinLauncher.getInstance().getModuleManager() != null) {
            MinLauncher.getInstance().getModuleManager().onTick();
        }
    }
}
