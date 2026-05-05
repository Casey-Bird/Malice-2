package net.maven.malady.core.items.soul;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SoulCrystal extends Item {

    // Properties that can be customized by subclasses
    protected final int effectDuration;  // in ticks
    protected final int effectAmplifier; // 0 = I, 1 = II, etc.
    protected final int maxUses;
    protected final int cooldownTicks;
    protected final Rarity rarity;
    protected final ChatFormatting nameColor;

    public SoulCrystal(Properties properties, SoulCrystalConfig config) {
        super(properties
                .stacksTo(config.stackSize)
                .rarity(config.rarity)
                .durability(config.maxUses)
        );

        this.effectDuration = config.effectDuration;
        this.effectAmplifier = config.effectAmplifier;
        this.maxUses = config.maxUses;
        this.cooldownTicks = config.cooldownTicks;
        this.rarity = config.rarity;
        this.nameColor = config.nameColor;
    }

    // Abstract method for custom effects (subclasses can override)
    protected void applyCustomEffects(Level level, Player player) {
        // Default: Apply strength
        player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_BOOST,
                effectDuration,
                effectAmplifier,
                false,  // ambient
                true,   // particles
                true    // icon
        ));
    }

    // Template method pattern - defines the algorithm
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            onUseClient(level, player, hand);
            return InteractionResultHolder.success(stack);
        }

        return onUseServer(level, player, hand, stack);
    }

    // Client-side behavior (can be overridden)
    protected void onUseClient(Level level, Player player, InteractionHand hand) {
        // Default: Play client-side effects
    }

    // Server-side behavior (can be overridden)
    protected InteractionResultHolder<ItemStack> onUseServer(Level level, Player player,
                                                             InteractionHand hand, ItemStack stack) {
        // Apply effects
        applyCustomEffects(level, player);

        // Play sound
        playUseSound(level, player);

        // Apply cooldown
        player.getCooldowns().addCooldown(this, cooldownTicks);

        // Remove 1
        stack.shrink(1);

        return InteractionResultHolder.success(stack);
    }

    protected void playUseSound(Level level, Player player) {
        // Default sound - can be overridden
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME,
                player.getSoundSource(),
                1.0F, 1.0F);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float)stack.getDamageValue() * 13.0F / (float)maxUses);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        // Default: Purple - can be overridden
        return 0x9933FF;
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        // Add effect info
        tooltip.add(Component.literal("Effect: Strength " + getRomanNumeral(effectAmplifier + 1))
                .withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("Duration: " + (effectDuration / 20) + "s")
                .withStyle(ChatFormatting.DARK_AQUA));

        // Add uses info
        tooltip.add(Component.literal("Uses: " + (maxUses - stack.getDamageValue()) + "/" + maxUses)
                .withStyle(ChatFormatting.DARK_GRAY));

        // Add cooldown info
        tooltip.add(Component.literal("Cooldown: " + (cooldownTicks / 20) + "s")
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack))
                .withStyle(nameColor);
    }

    // Utility method
    private String getRomanNumeral(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(number);
        };
    }

    // Config class for easy subclass configuration
    public static class SoulCrystalConfig {
        public int effectDuration = 600;      // 30 seconds default
        public int effectAmplifier = 0;       // Strength I default
        public int maxUses = 3;               // 3 uses default
        public int cooldownTicks = 200;       // 10 seconds default
        public int stackSize = 1;             // Stack size
        public Rarity rarity = Rarity.UNCOMMON;
        public ChatFormatting nameColor = ChatFormatting.LIGHT_PURPLE;

        // Builder pattern methods
        public SoulCrystalConfig setEffectDuration(int ticks) {
            this.effectDuration = ticks;
            return this;
        }

        public SoulCrystalConfig setEffectAmplifier(int amplifier) {
            this.effectAmplifier = amplifier;
            return this;
        }

        public SoulCrystalConfig setMaxUses(int uses) {
            this.maxUses = uses;
            return this;
        }

        public SoulCrystalConfig setCooldownTicks(int ticks) {
            this.cooldownTicks = ticks;
            return this;
        }

        public SoulCrystalConfig setStackSize(int size) {
            this.stackSize = size;
            return this;
        }

        public SoulCrystalConfig setRarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public SoulCrystalConfig setNameColor(ChatFormatting color) {
            this.nameColor = color;
            return this;
        }
    }
}