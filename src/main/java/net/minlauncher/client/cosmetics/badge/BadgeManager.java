package net.minlauncher.client.cosmetics.badge;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BadgeManager {
    private final Map<String, Badge> registeredBadges = new HashMap<>();
    private final Map<UUID, String> playerBadges = new HashMap<>();

    public void init() {
        // Registrar insignias predeterminadas estilo Lunar/Badlion
        registerBadge(new Badge("founder", "Fundador", "[★]", 0xFFFF5555, Identifier.fromNamespaceAndPath("minlauncher", "textures/badges/founder.png")));
        registerBadge(new Badge("vip", "VIP", "[✦]", 0xFFFFAA00, Identifier.fromNamespaceAndPath("minlauncher", "textures/badges/vip.png")));
        registerBadge(new Badge("pvp_master", "PvP Master", "[⚔]", 0xFF55FFFF, Identifier.fromNamespaceAndPath("minlauncher", "textures/badges/pvp.png")));
        registerBadge(new Badge("developer", "DEV", "[⚡]", 0xFF55FF55, Identifier.fromNamespaceAndPath("minlauncher", "textures/badges/dev.png")));
    }

    public void registerBadge(Badge badge) {
        registeredBadges.put(badge.getId(), badge);
    }

    public void setPlayerBadge(UUID uuid, String badgeId) {
        playerBadges.put(uuid, badgeId);
    }

    public Badge getBadgeForPlayer(UUID uuid) {
        String badgeId = playerBadges.get(uuid);
        if (badgeId != null) {
            return registeredBadges.get(badgeId);
        }
        // Por defecto todos los usuarios del cliente tienen la insignia del cliente activa
        return registeredBadges.get("founder");
    }

    public Map<String, Badge> getRegisteredBadges() {
        return registeredBadges;
    }
}