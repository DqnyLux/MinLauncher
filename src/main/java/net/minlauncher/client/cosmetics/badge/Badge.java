package net.minlauncher.client.cosmetics.badge;

import net.minecraft.resources.Identifier;

public class Badge {
    private final String id;
    private final String name;
    private final String prefixText;
    private final int colorHex;
    private final Identifier icon;

    public Badge(String id, String name, String prefixText, int colorHex, Identifier icon) {
        this.id = id;
        this.name = name;
        this.prefixText = prefixText;
        this.colorHex = colorHex;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefixText() {
        return prefixText;
    }

    public int getColorHex() {
        return colorHex;
    }

    public Identifier getIcon() {
        return icon;
    }
}