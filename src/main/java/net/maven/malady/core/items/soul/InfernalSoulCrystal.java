package net.maven.malady.core.items.soul;


import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;


public class InfernalSoulCrystal extends SoulCrystal {

    public InfernalSoulCrystal() {
        super(
                new Item.Properties(),
                new SoulCrystal.SoulCrystalConfig()
                        .setEffectDuration(600)
                        .setEffectAmplifier(0)
                        .setCooldownTicks(0)
                        .setStackSize(16)
                        .setRarity(Rarity.EPIC)
        );
    }

    @Override
    protected void applyCustomEffects(Level level, Player player) {

        player.addEffect(new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE,
                effectDuration,
                0,
                false,
                true,
                true
        ));

        // visual flame effect without damage
        // fire resistance prevents any actual damage
        player.igniteForSeconds(0.5f);
    }

    @Override
    protected void playUseSound(Level level, Player player) {

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.CAMPFIRE_CRACKLE,
                player.getSoundSource(),
                1.0F, 1.2F);


        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.FIRECHARGE_USE,
                player.getSoundSource(),
                0.8F, 1.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF4500;
    }

    @Override
    protected void onUseClient(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            for (int i = 0; i < 12; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 1.2;
                double offsetY = 0.5 + level.random.nextDouble() * 1.2;
                double offsetZ = (level.random.nextDouble() - 0.5) * 1.2;

                level.addParticle(
                        ParticleTypes.FLAME,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        0, 0.05, 0
                );

                level.addParticle(
                        ParticleTypes.SMOKE,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        (level.random.nextDouble() - 0.5) * 0.1,
                        0.1,
                        (level.random.nextDouble() - 0.5) * 0.1
                );
            }

            level.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    player.getX(),
                    player.getY() + 1.2,
                    player.getZ(),
                    0, 0.2, 0
            );
        }
    }
}