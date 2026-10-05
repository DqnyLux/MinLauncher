package net.minlauncher.client.module;

import net.minlauncher.client.module.setting.BooleanSetting;
import net.minlauncher.client.module.setting.ColorSetting;
import net.minlauncher.client.module.setting.NumberSetting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class HudModule extends Module {
    private int x;
    private int y;
    private int width;
    private int height;

    private final BooleanSetting background = new BooleanSetting("Fondo", "Muestra un fondo oscuro translúcido", true);
    private final BooleanSetting rounded = new BooleanSetting("Borde Redondo", "Suaviza las esquinas", true);
    private final ColorSetting textColor = new ColorSetting("Color Texto", "Color del texto", 0xFFFFFFFF);
    private final NumberSetting scale = new NumberSetting("Escala", "Tamaño del widget", 1.0, 0.5, 2.0, 0.1);

    public HudModule(String name, String description, int defaultX, int defaultY, int width, int height) {
        super(name, description, Category.HUD, 0);
        this.x = defaultX;
        this.y = defaultY;
        this.width = width;
        this.height = height;

        addSetting(background);
        addSetting(rounded);
        addSetting(textColor);
        addSetting(scale);
    }

    public abstract void render(GuiGraphicsExtractor extractor, float delta);

    public void renderDummy(GuiGraphicsExtractor extractor, float delta) {
        render(extractor, delta);
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + (width * scale.getValue()) &&
               mouseY >= y && mouseY <= y + (height * scale.getValue());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BooleanSetting getBackground() {
        return background;
    }

    public BooleanSetting getRounded() {
        return rounded;
    }

    public ColorSetting getTextColor() {
        return textColor;
    }

    public NumberSetting getScale() {
        return scale;
    }
}