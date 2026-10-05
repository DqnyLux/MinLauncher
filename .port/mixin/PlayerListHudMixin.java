package net.minlauncher.client.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.cosmetics.badge.Badge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void addClientBadgeToTab(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (MinLauncher.getInstance() == null || MinLauncher.getInstance().getBadgeManager() == null) return;

        Badge badge = MinLauncher.getInstance().getBadgeManager().getBadgeForPlayer(entry.getProfile().getId());
        if (badge != null) {
            Text original = cir.getReturnValue();
            MutableText badgeText = Text.literal(badge.getPrefixText() + " ").styled(style -> style.withColor(badge.getColorHex()));
            cir.setReturnValue(badgeText.append(original));
        }
    }
}
