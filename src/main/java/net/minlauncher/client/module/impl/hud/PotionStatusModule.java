package net.minlauncher.client.module.impl.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minlauncher.client.module.HudModule;
import net.minlauncher.client.ui.render.RenderUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** PotionStatus: lista de efectos de poción activos con duración y nivel (QOL legal). */
public class PotionStatusModule extends HudModule {

    private static final String[] LEVELS = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};

    public PotionStatusModule() {
        super("PotionStatus", "Muestra efectos de poción activos", 120, 120, 70, 14);
        setEnabled(true);
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, float delta) {
        if (mc.player == null) return;
        List<MobEffectInstance> effects = new ArrayList<>(mc.player.getActiveEffects());
        effects.sort((a, b) -> a.getEffect().value().getDisplayName().getString()
                .compareToIgnoreCase(b.getEffect().value().getDisplayName().getString()));
        if (effects.isEmpty()) {
            setWidth(10);
            setHeight(0);
            return;
        }

        List<Integer> colors = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        int maxW = 0;
        for (MobEffectInstance e : effects) {
            colors.add(e.getEffect().value().getColor());
            String name = e.getEffect().value().getDisplayName().getString();
            int amp = e.getAmplifier();
            if (amp >= 0 && amp < LEVELS.length) name += " " + LEVELS[amp];
            int sec = e.getDuration() / 20;
            int m = sec / 60, s = sec % 60;
            name += " " + m + ":" + String.format("%02d", s);
            lines.add(name);
            maxW = Math.max(maxW, mc.font.width(name));
        }

        int lineH = 11;
        setWidth(maxW + 14);
        setHeight(lines.size() * lineH + 4);
        if (getBackground().getValue()) {
            RenderUtils.drawRect(extractor, getX(), getY(), getWidth(), getHeight(), 0x66000000);
        }
        int rowY = getY() + 2;
        for (int i = 0; i < lines.size(); i++) {
            int c = colors.get(i);
            extractor.fill(getX() + 3, rowY + 2, getX() + 7, rowY + 6, c);
            extractor.text(mc.font, lines.get(i), getX() + 10, rowY, getTextColor().getEffectiveColor(), true);
            rowY += lineH;
        }
    }
}