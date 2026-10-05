package net.minlauncher.client.cosmetics;

public enum CosmeticType {
    CAPE("Capa", "Capas personalizadas y animadas estilo Lunar"),
    WINGS("Alas 3D", "Alas animadas de dragón o ángel con aleteo físico"),
    BANDANA("Bandana", "Accesorio facial 3D en la cabeza"),
    HAT("Gorro / Corona", "Cosmético superior 3D"),
    BADGE("Insignia / Icono", "Insignia visual en TabList, Chat y Nametags");

    private final String displayName;
    private final String description;

    CosmeticType(String displayName, String description) {
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
