package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArmorStatusModule extends HudModule {

    public ArmorStatusModule() {
        super("Armor Status", "Muestra la armadura equipada y su durabilidad", 10, 120, 20, 60);
        setEnabled(true);
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        if (mc.player == null) return;

        // Recorremos del casco (head) a las botas (feet): 4-1
        Map<EquipmentSlot, ItemStack> armor = new LinkedHashMap<>();
        armor.put(EquipmentSlot.HEAD, mc.player.getItemBySlot(EquipmentSlot.HEAD));
        armor.put(EquipmentSlot.CHEST, mc.player.getItemBySlot(EquipmentSlot.CHEST));
        armor.put(EquipmentSlot.LEGS, mc.player.getItemBySlot(EquipmentSlot.LEGS));
        armor.put(EquipmentSlot.FEET, mc.player.getItemBySlot(EquipmentSlot.FEET));

        int curY = getY();
        for (ItemStack stack : armor.values()) {
            if (!stack.isEmpty()) {
                String label = stack.getHoverName().getString();
                if (stack.isDamageableItem()) {
                    int max = stack.getMaxDamage();
                    int current = max - stack.getDamageValue();
                    int percent = (int) (((double) current / max) * 100);
                    label += "  " + percent + "%";
                }

                String text = label;
                int textWidth = mc.font.width(text);
                setWidth(Math.max(getWidth(), textWidth + 8));
                RenderUtils.drawRect(extractor, getX(), curY, textWidth + 6, 12, 0x66000000);
                extractor.text(mc.font, text, getX() + 3, curY + 2, getTextColor().getEffectiveColor(), true);
                curY += 14;
            }
        }

        setHeight(Math.max(20, curY - getY()));
    }
}