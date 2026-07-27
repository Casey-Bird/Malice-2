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

public class MalignantSoulCrystal extends SoulCrystal {

    public MalignantSoulCrystal() {
        super(
                new Item.Properties(),
                new SoulCrystal.SoulCrystalConfig()
                        .setEffectDuration(600)
                        .setEffectAmplifier(4)
                        .setCooldownTicks(0)
                        .setStackSize(16)
                        .setRarity(Rarity.UNCOMMON)
                        .setNameColor(ChatFormatting.DARK_PURPLE)
        );
    }

    @Override
    protected void applyCustomEffects(Level level, Player player) {
        player.addEffect(new MobEffectInstance(
                MobEffects.JUMP,
                effectDuration,
                effectAmplifier,
                false,
                true,
                true
        ));
        player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,
                effectDuration / 2,
                0,
                false,
                true,
                true
        ));
    }

    @Override
    protected void playUseSound(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SCULK_BLOCK_SPREAD,
                player.getSoundSource(),
                1.0F, 0.7F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SPAWN,
                player.getSoundSource(),
                0.5F, 1.5F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x7B2FBE;
    }

    @Override
    protected void onUseClient(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(
                        ParticleTypes.SCULK_SOUL,
                        player.getX() + (level.random.nextDouble() - 0.5) * 1.8,
                        player.getY() + 0.3 + level.random.nextDouble() * 1.2,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 1.8,
                        (level.random.nextDouble() - 0.5) * 0.15,
                        0.05 + level.random.nextDouble() * 0.1,
                        (level.random.nextDouble() - 0.5) * 0.15
                );
                level.addParticle(
                        ParticleTypes.SMALL_GUST,
                        player.getX() + (level.random.nextDouble() - 0.5) * 1.0,
                        player.getY() + 1.0,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 1.0,
                        0.0, -0.02, 0.0
                );
            }
        }
    }
}