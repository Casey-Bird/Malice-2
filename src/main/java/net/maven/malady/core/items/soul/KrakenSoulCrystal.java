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

public class KrakenSoulCrystal extends SoulCrystal {

    public KrakenSoulCrystal() {
        super(
                new Item.Properties(),
                new SoulCrystal.SoulCrystalConfig()
                        .setEffectDuration(1200)
                        .setEffectAmplifier(4)
                        .setCooldownTicks(0)
                        .setStackSize(64)
                        .setRarity(Rarity.EPIC)
                        .setNameColor(ChatFormatting.BLUE)
        );
    }

    @Override
    protected void applyCustomEffects(Level level, Player player) {
        player.addEffect(new MobEffectInstance(
                MobEffects.DOLPHINS_GRACE,
                effectDuration,
                effectAmplifier,
                false,
                true,
                true
        ));
        player.addEffect(new MobEffectInstance(
                MobEffects.WATER_BREATHING,
                effectDuration,
                0,
                false,
                true,
                true
        ));
    }

    @Override
    protected void playUseSound(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_SPLASH,
                player.getSoundSource(),
                1.0F, 1.0F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
                player.getSoundSource(),
                0.8F, 1.2F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x3366CC;
    }

    @Override
    protected void onUseClient(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        if (level.isClientSide) {
            for (int i = 0; i < 12; i++) {
                level.addParticle(
                        ParticleTypes.BUBBLE,
                        player.getX() + (level.random.nextDouble() - 0.5) * 2.0,
                        player.getY() + 0.5 + level.random.nextDouble() * 1.5,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 2.0,
                        (level.random.nextDouble() - 0.5) * 0.2,
                        0.1 + level.random.nextDouble() * 0.2,
                        (level.random.nextDouble() - 0.5) * 0.2
                );
                level.addParticle(
                        ParticleTypes.SPLASH,
                        player.getX() + (level.random.nextDouble() - 0.5) * 1.5,
                        player.getY() + 0.2,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 1.5,
                        (level.random.nextDouble() - 0.5) * 0.1,
                        0.1,
                        (level.random.nextDouble() - 0.5) * 0.1
                );
            }
        }
    }
}