package net.minlauncher.client.cosmetics;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CosmeticsManager {
    private final List<Cosmetic> cosmetics = new ArrayList<>();

    public void init() {
        // Capas predeterminadas
        register(new Cosmetic("cape_galaxy", "Capa Galaxia", CosmeticType.CAPE, Identifier.of("minlauncher", "textures/cosmetics/capes/galaxy.png")));
        register(new Cosmetic("cape_lunar_style", "Capa Lunar Dark", CosmeticType.CAPE, Identifier.of("minlauncher", "textures/cosmetics/capes/dark.png")));
        register(new Cosmetic("cape_fire", "Capa de Fuego", CosmeticType.CAPE, Identifier.of("minlauncher", "textures/cosmetics/capes/fire.png")));

        // Alas 3D
        register(new Cosmetic("wings_dragon", "Alas de Dragón", CosmeticType.WINGS, Identifier.of("minlauncher", "textures/cosmetics/wings/dragon.png")));
        register(new Cosmetic("wings_angel", "Alas Angelicales", CosmeticType.WINGS, Identifier.of("minlauncher", "textures/cosmetics/wings/angel.png")));

        // Bandana y Gorros
        register(new Cosmetic("bandana_ninja", "Bandana Ninja", CosmeticType.BANDANA, Identifier.of("minlauncher", "textures/cosmetics/bandanas/ninja.png")));
        register(new Cosmetic("hat_crown", "Corona Dorada", CosmeticType.HAT, Identifier.of("minlauncher", "textures/cosmetics/hats/crown.png")));

        // Equipar por defecto una capa y alas para testing
        if (!cosmetics.isEmpty()) {
            cosmetics.get(0).setEquipped(true);
        }
    }

    public void register(Cosmetic cosmetic) {
        this.cosmetics.add(cosmetic);
    }

    public List<Cosmetic> getCosmetics() {
        return Collections.unmodifiableList(cosmetics);
    }

    public List<Cosmetic> getCosmeticsByType(CosmeticType type) {
        return cosmetics.stream()
                .filter(c -> c.getType() == type)
                .collect(Collectors.toList());
    }

    public Cosmetic getEquipped(CosmeticType type) {
        return cosmetics.stream()
                .filter(c -> c.getType() == type && c.isEquipped())
                .findFirst()
                .orElse(null);
    }

    public void equipCosmetic(Cosmetic cosmetic) {
        // Desequipar el cosmético del mismo tipo si existe
        for (Cosmetic c : cosmetics) {
            if (c.getType() == cosmetic.getType()) {
                c.setEquipped(false);
            }
        }
        cosmetic.setEquipped(true);
    }

    public void unequipCosmetic(Cosmetic cosmetic) {
        cosmetic.setEquipped(false);
    }
}
