package net.minlauncher.client.module.impl.pvp;

import net.minecraft.world.entity.player.Player;
import net.minlauncher.client.module.Module;
import net.minlauncher.client.module.Category;

/**
 * KeepSprint (PvP, 100% legal): mientras atacas y estabas esprintando,
 * el sprint se mantiene aunque sueltes W (estilo STM). No modifica daño
 * ni hitboxes: solo el estado de sprint del jugador.
 */
public class KeepSprintModule extends Module {
    private boolean locked;

    public KeepSprintModule() {
        super("KeepSprint", "Mantén el sprint mientras peleas aunque sueltes W", Category.PVP, 0);
    }

    @Override
    public void onTick() {
        Player player = mc.player;
        if (player == null) { locked = false; return; }
        boolean attacking = mc.options.keyAttack.isDown();

        if (attacking && player.isSprinting()) {
            locked = true; // bloqueamos el sprint mientras sigamos atacando
        }
        if (locked) {
            player.setSprinting(true);
            if (!attacking) locked = false;
        }
    }
}