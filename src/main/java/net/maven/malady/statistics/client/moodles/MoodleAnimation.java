package net.maven.malady.statistics.client.moodles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class MoodleAnimation {
    private final MoodleType type;
    private MoodleStage currentStage;
    private float alpha = 0f;
    private int xOffset = 0;
    private boolean active = false;

    public MoodleAnimation(MoodleType type) {
        this.type = type;
    }

    public void update(Player player) {
        MoodleStage newStage = type.checkStage(player);

        if (newStage != currentStage) {
            // Stage changed - reset animation
            currentStage = newStage;
            active = (newStage != null);
            alpha = 0f;
            xOffset = 10;
        }

        if (active) {
            // Animate in
            alpha = Math.min(1f, alpha + 0.05f);
            xOffset = (int)Math.max(0, xOffset - 0.5f);
        } else if (alpha > 0) {
            // Animate out
            alpha = Math.max(0f, alpha - 0.05f);
            if (alpha <= 0) {
                currentStage = null;
            }
        }
    }

    public boolean isActive() {
        return alpha > 0;
    }

    public MoodleType getType() {
        return type;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getXOffset() {
        return xOffset;
    }

    public MoodleStage getCurrentStage() {
        return currentStage;
    }

    public ResourceLocation getCurrentTexture() {
        return type.getTextureForStage(currentStage);
    }
}


