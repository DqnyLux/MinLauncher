package net.minlauncher.client.cosmetics;

import net.minecraft.util.Identifier;

public class Cosmetic {
    private final String id;
    private final String name;
    private final CosmeticType type;
    private final Identifier texture;
    private boolean equipped;
    private boolean animated;

    public Cosmetic(String id, String name, CosmeticType type, Identifier texture) {
        this(id, name, type, texture, false);
    }

    public Cosmetic(String id, String name, CosmeticType type, Identifier texture, boolean animated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.texture = texture;
        this.equipped = false;
        this.animated = animated;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CosmeticType getType() {
        return type;
    }

    public Identifier getTexture() {
        return texture;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public boolean isAnimated() {
        return animated;
    }
}
