package net.minlauncher.client.module;

public enum Category {
    PVP("PvP", "Módulos de combate y respuesta rápida"),
    SURVIVAL("Survival", "Módulos de supervivencia, exploración y utilidad"),
    HUD("HUD", "Elementos visuales en pantalla y estadísticas"),
    RENDER("Render", "Modificaciones visuales del mundo"),
    COSMETICS("Cosméticos", "Capas, alas, gorros e insignias");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
