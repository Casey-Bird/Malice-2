package net.maven.malady.core.items.soul;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BrimstoneSoulCrystal extends SoulCrystal {


    public BrimstoneSoulCrystal() {
        super(
                new Item.Properties(),
                new SoulCrystal.SoulCrystalConfig()
                        .setEffectDuration(600)      // 30 seconds (20 ticks per second)
                        .setEffectAmplifier(2)       // Digging Speed III (0 = Speed I, 1 = Speed II, 2 = Speed III)
                        .setMaxUses(1)               // Single use
                        .setCooldownTicks(0)          // No cooldown (or adjust as needed)
                        .setStackSize(16)             // Can stack to 16
                        .setRarity(Rarity.COMMON)     // Common rarity
                        .setNameColor(ChatFormatting.YELLOW)  // Yellow name
        );
    }

    @Override
    protected void applyCustomEffects(Level level, Player player) {
        // Apply default crystal buff (Digging Speed III)
        super.applyCustomEffects(level, player);

        // Add Glowing effect for half duration
        player.addEffect(new MobEffectInstance(
                MobEffects.GLOWING,
                effectDuration / 2,  // Half duration
                0,                   // Amplifier 0 (Glowing I)
                false,
                true,
                true
        ));

        // Add exhaustion (hunger cost)
        player.causeFoodExhaustion(0.2F);
    }

    @Override
    protected void playUseSound(Level level, Player player) {
        // Crunchy sound effect
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EAT,
                player.getSoundSource(),
                1.0F, 0.8F);  // Slightly lower pitch for a crunchier feel

        // Add a secondary crunch sound for more texture
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ROOTED_DIRT_BREAK,
                player.getSoundSource(),
                0.5F, 1.2F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        // Gray durability bar
        return 0x888888;
    }

    protected void onUseClient(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        // Client-side sandy particles
        if (level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                // Sandy particles around the player
                level.addParticle(
                        ParticleTypes.INFESTED,
                        player.getX() + (level.random.nextDouble() - 0.5) * 1.5,
                        player.getY() + 0.5 + level.random.nextDouble() * 1.5,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 1.5,
                        (level.random.nextDouble() - 0.5) * 0.1,
                        0.1 + level.random.nextDouble() * 0.1,
                        (level.random.nextDouble() - 0.5) * 0.1
                );

                // Additional falling dust particles for sandy effect
                level.addParticle(
                        ParticleTypes.DUST_PLUME,
                        player.getX() + (level.random.nextDouble() - 0.5) * 1.0,
                        player.getY() + 2.0,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 1.0,
                        0.0, -0.05, 0.0
                );
            }
        }
    }

}
