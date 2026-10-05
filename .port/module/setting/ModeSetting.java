package net.minlauncher.client.module.setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {
    private final List<String> modes;
    private int index;

    public ModeSetting(String name, String description, String defaultMode, String... modes) {
        super(name, description, defaultMode);
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
        if (this.index == -1) {
            this.index = 0;
            this.value = this.modes.get(0);
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public void cycle() {
        if (modes.isEmpty()) return;
        index = (index + 1) % modes.size();
        this.value = modes.get(index);
    }

    public boolean is(String mode) {
        return this.value.equalsIgnoreCase(mode);
    }
}
