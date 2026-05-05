package net.maven.malady.statistics.client;


import com.alrex.parcool.api.Stamina;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.maven.malady.Malady;
import net.maven.malady.currency.items.CurrencyItems;
import net.maven.malady.statistics.client.moodles.MoodleAnimation;
import net.maven.malady.statistics.client.moodles.MoodleContainer;
import net.maven.malady.statistics.client.moodles.MoodleStage;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

@EventBusSubscriber(modid = Malady.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class PlayerHUDOverlay {

    // Make the container a static field so it persists between frames
    private static MoodleContainer moodleContainer = new MoodleContainer();

    public static int currencyAmount = 0;

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) return;

        int screenWidth = event.getGuiGraphics().guiWidth();
        int screenHeight = event.getGuiGraphics().guiHeight();


        renderPlayerStats(event.getGuiGraphics(), player, screenWidth, screenHeight);

        renderHealthBar(event.getGuiGraphics(), player, event.getPartialTick());

        renderStaminaBar(event.getGuiGraphics(), player, event.getPartialTick());

        drawMoodles(event.getGuiGraphics(), player, screenWidth, screenHeight);

        renderCurrency(event.getGuiGraphics(), screenWidth, screenHeight);

        renderPlayerHead(event.getGuiGraphics(), player, 100, 100, 32);
    }

    // Player statistics
    private static final ResourceLocation STATS_TEXTURE = ResourceLocation.fromNamespaceAndPath(Malady.MODID, "textures/gui/statistics/player_stats.png"
    );

    // Moodles
    private static final ResourceLocation MOODLE_BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath("malady", "textures/gui/statistics/moodle/base.png");


    private static void renderPlayerStats(GuiGraphics guiGraphics, Player player, int screenWidth, int screenHeight) {
        int hunger = player.getFoodData().getFoodLevel(); // Get hunger level
        int armor = player.getArmorValue(); // Get armor rating // TODO Should this number just be unrendered?


        // Define position (Top-left)
        int x = 10;
        int y = 10;

        // Bind texture and render it
        RenderSystem.setShaderTexture(0, STATS_TEXTURE);
        guiGraphics.blit(STATS_TEXTURE, x, y, 0, 0, 100, 50, 100, 50); // Adjust dimensions based on your texture

    }

    private static void renderCurrency(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        // Position relative to HUD (x10, y15)
        int x = 53;
        int y = 46;

        ItemStack currencyItemStack = new ItemStack(CurrencyItems.CURRENCY_ITEM.asItem());

        // Apply scaling transformation
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0); // Move to position
        guiGraphics.pose().scale(0.6f, 0.6f, 1.0f); // Scale down to 50%
        guiGraphics.renderFakeItem(currencyItemStack, 0, 0); // Render at (0,0) relative to translation
        guiGraphics.pose().popPose();

        // Adjust text position based on scaled icon size
        String currencyText = String.valueOf(currencyAmount);
        guiGraphics.drawString(Minecraft.getInstance().font, currencyText, x + 12, y + 1, 0xFFFFE0E0); // White color
    }

    public static void drawMoodles(GuiGraphics guiGraphics, Player player, int screenWidth, int screenHeight) {
        // Update the existing container instead of creating a new one
        moodleContainer.update(player);

        List<MoodleAnimation> activeMoodles = moodleContainer.getActiveMoodles();
        int baseX = 18;
        int baseY = 48;

        for (int i = 0; i < activeMoodles.size(); i++) {
            MoodleAnimation moodle = activeMoodles.get(i);
            MoodleStage stage = moodle.getCurrentStage();

            if (stage == null) continue;

            int yOffset = baseY + (i * 27);
            int xOffset = baseX + moodle.getXOffset();
            float alpha = moodle.getAlpha();

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

            // Debug logging to verify textures are loading
            ResourceLocation texture = moodle.getCurrentTexture();

            guiGraphics.blit(
                    texture,
                    xOffset, yOffset,
                    0, 0,
                    25, 25,
                    25, 25
            );

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableBlend();
        }
    }

    ///// ======= Health Bar ======= /////

    // Health Bar Settings
    private static final ResourceLocation BAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Malady.MODID, "textures/gui/statistics/bar.png");
    private static final int SEGMENT_WIDTH = 58;  // Width of each health segment
    private static final int BAR_HEIGHT = 5;      // Height of health bar
    private static final int BAR_X = 50;           // X position
    private static final int BAR_Y = 16;           // Y position
    private static final int MAX_SEGMENTS = 5;     // 20hp per segment * 5 = 100hp max

    // Health Colors (RGBA format)
    private static final int[] HEALTH_COLORS = {
            0xFFFF0000, // Red (0-20hp)
            0xFFFFA500, // Orange (20-40hp)
            0xFFFFFF00, // Yellow (40-60hp)
            0xFF00FF00, // Green (60-80hp)
            0xFF00FFFF  // Cyan (80-100hp)
    };

    // Animation variables
    private static float displayedHealth = 0f;
    private static float healthChangeTimer = 0f;
    private static float shakeIntensity = 0f;
    private static final float HEALTH_CHANGE_SPEED = 0.75f;
    private static final float SHAKE_DURATION = 0.5f;
    private static final float MAX_SHAKE_OFFSET = 3f;

    private static void renderHealthBar(GuiGraphics guiGraphics, Player player, DeltaTracker deltaTracker) {
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);

        // Smooth health animation
        float targetHealth = player.getHealth();
        float maxHealth = player.getMaxHealth();

        // Animate health change
        if (displayedHealth < targetHealth) {
            displayedHealth = Math.min(displayedHealth + HEALTH_CHANGE_SPEED * partialTicks, targetHealth);
        } else if (displayedHealth > targetHealth) {
            displayedHealth = Math.max(displayedHealth - HEALTH_CHANGE_SPEED * partialTicks, targetHealth);
            // Trigger shake effect when losing health
            healthChangeTimer = SHAKE_DURATION;
        }

        // Update shake effect
        if (healthChangeTimer > 0) {
            healthChangeTimer -= partialTicks;
            shakeIntensity = MAX_SHAKE_OFFSET * (healthChangeTimer / SHAKE_DURATION);
        } else {
            shakeIntensity = 0f;
        }

        // Calculate shake offset
        float shakeOffsetX = (float)(Math.random() * 2 - 1) * shakeIntensity;
        float shakeOffsetY = (float)(Math.random() * 2 - 1) * shakeIntensity;

        // Render each health segment
        for (int segment = 0; segment < MAX_SEGMENTS; segment++) {
            float segmentMinHealth = segment * 20f;
            float segmentMaxHealth = (segment + 1) * 20f;

            // Calculate how much of this segment is filled
            float segmentFill = 0f;
            if (displayedHealth > segmentMinHealth) {
                segmentFill = Math.min((displayedHealth - segmentMinHealth) / 20f, 1f);
            }

            // Only render if this segment has any health
            if (segmentFill > 0) {
                int color = HEALTH_COLORS[segment % HEALTH_COLORS.length];

                // Apply brightness pulse when health is low
                float brightness = 1f;
                if (segment == 0 && displayedHealth < 10) {
                    brightness = 1f + 0.5f * (float)Math.sin(System.currentTimeMillis() / 200f);
                }

                // Extract color components
                float red = ((color >> 16) & 0xFF) / 255f * brightness;
                float green = ((color >> 8) & 0xFF) / 255f * brightness;
                float blue = (color & 0xFF) / 255f * brightness;
                float alpha = ((color >> 24) & 0xFF) / 255f;

                // Set color with shake offset
                RenderSystem.setShaderColor(red, green, blue, alpha);
                RenderSystem.enableBlend();

                // Calculate position with shake
                int xPos = BAR_X + (segment * SEGMENT_WIDTH) + (int)shakeOffsetX;
                int yPos = BAR_Y + (int)shakeOffsetY;

                // Render the segment
                guiGraphics.blit(BAR_TEXTURE,
                        xPos, yPos,
                        0, 0,
                        (int)(SEGMENT_WIDTH * segmentFill), BAR_HEIGHT,
                        SEGMENT_WIDTH, BAR_HEIGHT);

                // Reset color
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            }
        }
    }

    // Rendering the stamina bar from ParCool into the player HUD
    private static void renderStaminaBar(GuiGraphics guiGraphics, Player player, DeltaTracker deltaTracker) {
        Stamina stamina = Stamina.get(player);
        if (stamina == null) return;

        int maxStamina = stamina.getMaxValue();
        int currentStamina = stamina.getValue();

        // Calculate how much of the stamina bar should be filled (0-1 range)
        float staminaFill = (float) currentStamina / maxStamina;

        // Position: 5 pixels below the health bar
        int staminaBarY = BAR_Y + BAR_HEIGHT + 5;

        // Use a single segment width for stamina bar (same as one health segment)
        int staminaBarWidth = (int)(SEGMENT_WIDTH * staminaFill);

        // Use a different color for stamina (light blue/green)
        float red = 1.0f;
        float green = 1.0f;
        float blue = 0.2f;
        float alpha = 1.0f;

        RenderSystem.setShaderColor(red, green, blue, alpha);
        RenderSystem.enableBlend();

        // Render the stamina bar (single segment width)
        guiGraphics.blit(BAR_TEXTURE,
                BAR_X, staminaBarY,  // Same X position as health bar
                0, 0,
                staminaBarWidth, BAR_HEIGHT,
                SEGMENT_WIDTH, BAR_HEIGHT);

        // Reset color
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        // Optional: Add a background bar to show max stamina (same single segment width)
        RenderSystem.setShaderColor(0.3f, 0.3f, 0.3f, 0.5f); // Gray semi-transparent
        guiGraphics.blit(BAR_TEXTURE,
                BAR_X + staminaBarWidth, staminaBarY,
                0, 0,
                SEGMENT_WIDTH - staminaBarWidth, BAR_HEIGHT,
                SEGMENT_WIDTH, BAR_HEIGHT);

        // Reset color
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }


    private static void renderPlayerHead(GuiGraphics guiGraphics, Player player, int x, int y, float size) {
        if (player == null) return;

        Minecraft minecraft = Minecraft.getInstance();
        PlayerSkin skin = minecraft.getSkinManager().getInsecureSkin(player.getGameProfile());
        ResourceLocation skinTexture = skin.texture();

        guiGraphics.pose().pushPose();

        // Make it 20% smaller
        float smallerSize = size * 0.8f;

        // Center it
        float centerOffset = (size - smallerSize) / 2f;

        guiGraphics.pose().translate(x + centerOffset, y + centerOffset, 0);

        // Scale
        float scale = smallerSize / 7.8f;
        guiGraphics.pose().scale(scale, scale, 1);

        // Draw
        guiGraphics.blit(
                skinTexture,
                -26, -27,
                8, 8, 8, 8,
                64, 64
        );

        guiGraphics.pose().popPose();
    }



}

