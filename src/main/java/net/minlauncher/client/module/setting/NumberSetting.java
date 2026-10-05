package net.minlauncher.client.module.setting;

public class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double increment;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double increment) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    @Override
    public void setValue(Double value) {
        double clamped = Math.max(min, Math.min(max, value));
        double precision = 1.0 / increment;
        this.value = Math.round(clamped * precision) / precision;
    }

    public float getFloatValue() {
        return value.floatValue();
    }

    public int getIntValue() {
        return value.intValue();
    }
}