package net.minlauncher.client.module.setting;

import java.awt.Color;

public class ColorSetting extends Setting<Integer> {
    private boolean rainbow;

    public ColorSetting(String name, String description, int defaultRgbaHex) {
        super(name, description, defaultRgbaHex);
        this.rainbow = false;
    }

    public ColorSetting(String name, String description, int defaultRgbaHex, boolean rainbow) {
        super(name, description, defaultRgbaHex);
        this.rainbow = rainbow;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public int getEffectiveColor() {
        if (rainbow) {
            float hue = (System.currentTimeMillis() % 4000L) / 4000.0f;
            int rgb = Color.HSBtoRGB(hue, 0.8f, 1.0f);
            return (this.value & 0xFF000000) | (rgb & 0x00FFFFFF);
        }
        return this.value;
    }
}