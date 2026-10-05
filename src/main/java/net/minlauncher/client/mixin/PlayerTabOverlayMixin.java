package net.minlauncher.client.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.cosmetics.badge.Badge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    private void minlauncher$addClientBadgeToTab(PlayerInfo entry, CallbackInfoReturnable<Component> cir) {
        if (MinLauncher.getInstance() == null || MinLauncher.getInstance().getBadgeManager() == null) return;

        Badge badge = MinLauncher.getInstance().getBadgeManager().getBadgeForPlayer(entry.getProfile().id());
        if (badge != null) {
            Component original = cir.getReturnValue();
            MutableComponent badgeText = Component.literal(badge.getPrefixText() + " ")
                    .withStyle(style -> style.withColor(badge.getColorHex()));
            cir.setReturnValue(badgeText.append(original));
        }
    }
}