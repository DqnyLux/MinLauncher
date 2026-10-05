package net.minlauncher.client.module.impl.survival;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minlauncher.client.module.Category;
import net.minlauncher.client.module.Module;

/**
 * AutoTool (Survival, 100% legal): mientras rompes (tecla de ataque pulsada
 * sobre un bloque), cambia automaticamente a la mejor herramienta de la
 * barra rapida que tenga mayor velocidad de extraccion. No toca el servidor:
 * solo selecciona tu slot.
 */
public class AutoToolModule extends Module {
    public AutoToolModule() {
        super("AutoTool", "Cambia a la mejor herramienta al romper bloques", Category.SURVIVAL, 0);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (!mc.options.keyAttack.isDown()) return;
        if (!(mc.hitResult instanceof BlockHitResult hit)) return;

        BlockState state = mc.level.getBlockState(hit.getBlockPos());
        if (state.isAir()) return;

        Inventory inv = mc.player.getInventory();
        int selected = inv.getSelectedSlot();
        float bestSpeed = inv.getItem(selected).getDestroySpeed(state);
        int bestSlot = -1;

        for (int i = 0; i < 9; i++) {
            if (i == selected) continue;
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            float speed = stack.getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        if (bestSlot != -1) {
            inv.setSelectedSlot(bestSlot);
        }
    }
}