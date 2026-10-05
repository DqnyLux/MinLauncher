package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.module.setting.BooleanSetting;

public class ArmorStatusModule extends HudModule {
    private final BooleanSetting showDamage = new BooleanSetting("Mostrar Durabilidad", "Muestra el porcentaje o daño restante", true);

    public ArmorStatusModule() {
        super("Armor Status", "Muestra la armadura equipada y su durabilidad", 10, 120, 20, 80);
        addSetting(showDamage);
        setEnabled(true);
    }

    @Override
    public void render(DrawContext context, float delta) {
        if (mc.player == null) return;

        int curY = getY();
        int curX = getX();

        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.player.getInventory().getArmorStack(i);
            if (!stack.isEmpty()) {
                context.drawItem(stack, curX, curY);
                context.drawItemInSlot(mc.textRenderer, stack, curX, curY);

                if (showDamage.getValue() && stack.isDamageable()) {
                    int max = stack.getMaxDamage();
                    int current = max - stack.getDamage();
                    int percent = (int) (((double) current / max) * 100);
                    String text = percent + "%";
                    context.drawText(mc.textRenderer, text, curX + 18, curY + 4, getTextColor().getEffectiveColor(), true);
                }
                curY += 18;
            }
        }

        setWidth(60);
        setHeight(Math.max(20, curY - getY()));
    }
}
