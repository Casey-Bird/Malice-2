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

public class FrostedSoulCrystal extends SoulCrystal {

    public FrostedSoulCrystal() {
        super(
                new Item.Properties(),
                new SoulCrystal.SoulCrystalConfig()
                        .setEffectDuration(600)
                        .setEffectAmplifier(0)
                        .setMaxUses(1)
                        .setCooldownTicks(0)
                        .setStackSize(16)
                        .setRarity(Rarity.UNCOMMON)
                        .setNameColor(ChatFormatting.AQUA)
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

        player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                effectDuration / 2,
                1,
                false,
                true,
                true
        ));

        player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,
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
                SoundEvents.GLASS_BREAK,
                player.getSoundSource(),
                1.0F, 1.5F);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOW_BREAK,
                player.getSoundSource(),
                0.8F, 1.2F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x66CCFF;
    }

    @Override
    protected void onUseClient(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            for (int i = 0; i < 15; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 1.5;
                double offsetY = 0.5 + level.random.nextDouble() * 1.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 1.5;

                level.addParticle(
                        ParticleTypes.ITEM_SNOWBALL,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        (level.random.nextDouble() - 0.5) * 0.2,
                        0.1,
                        (level.random.nextDouble() - 0.5) * 0.2
                );

                level.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        0, -0.05, 0
                );
            }

            level.addParticle(
                    ParticleTypes.ITEM_SNOWBALL,
                    player.getX(),
                    player.getY() + 1.5,
                    player.getZ(),
                    0, 0.2, 0
            );
        }
    }
}
