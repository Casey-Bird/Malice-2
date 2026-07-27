package net.maven.malady.core.items.soul;

import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class LesserSoulCrystal extends SoulCrystal {

    public LesserSoulCrystal() {
        super(
                new Properties(),
                new SoulCrystalConfig()
                        .setEffectDuration(400)      // 20 seconds
                        .setEffectAmplifier(0)       // Strength I
                        .setCooldownTicks(300)       // 15 seconds cooldown
                        .setStackSize(16)            // Can stack to 16
                        .setRarity(Rarity.COMMON)    // Common rarity
                        .setNameColor(ChatFormatting.GRAY)  // Gray name
        );
    }

    @Override
    protected void applyCustomEffects(Level level, Player player) {
        // Apply default crystal buff
        super.applyCustomEffects(level, player);

        // Lesser version also gives minor side effects
//        player.addEffect(new MobEffectInstance(
//                MobEffects.MOVEMENT_SPEED,
//                effectDuration / 2,  // Half duration
//                0,                   // Speed I
//                false,
//                true,
//                true
//        ));
//
//        // Add exhaustion (hunger cost)
//        player.causeFoodExhaustion(0.3F);
    }

    @Override
    protected void playUseSound(Level level, Player player) {
        // Different sound for lesser version
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GLASS_BREAK,
                player.getSoundSource(),
                0.7F, 1.2F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        // Gray durability bar for lesser version
        return 0x888888;
    }

    @Override
    protected void onUseClient(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        // Client-side particles for lesser version
        if (level.isClientSide) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(
                        net.minecraft.core.particles.ParticleTypes.CLOUD,
                        player.getX() + (level.random.nextDouble() - 0.5) * 0.5,
                        player.getY() + 1.0,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 0.5,
                        0.0, 0.05, 0.0
                );
            }
        }
    }
}