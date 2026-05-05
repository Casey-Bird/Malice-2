package net.maven.malady.mixins.bonded;


import com.iamkaf.amber.api.item.SmartTooltip;
import com.iamkaf.bonded.Bonded;
import com.iamkaf.bonded.mixin.ItemMixin;
import com.iamkaf.bonded.registry.DataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class BondedTooltipMixin {


    @Inject(at = @At("HEAD"), method = "appendHoverText")
    public void addBondedTooltip(ItemStack stack, Item.TooltipContext context,
                                             List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (Bonded.CONFIG.enableTooltips.get()) {
            return;
        }

        var levelingComponent = stack.get(DataComponents.ITEM_LEVEL_CONTAINER.get());

        if (levelingComponent == null) {
            return;
        }

        tooltipComponents.add(Component.literal("Bond " + levelingComponent.getBond() + "\ueef2")
                .withStyle(ChatFormatting.RED));

        // TODO Keeping this for now to make sure it's disabled
        if (tooltipFlag.isAdvanced()) {
            var bonuses = stack.get(DataComponents.APPLIED_BONUSES_CONTAINER.get());
            MutableComponent bonusesTooltip = Component.literal(String.format("Bonuses (%s):",
                    bonuses == null ? 0 : bonuses.bonuses().size()
            ));
            SmartTooltip smartTooltip = new SmartTooltip().shift(bonusesTooltip);
            if (bonuses != null) {
                for (var bonus : bonuses.bonuses()) {
                    smartTooltip.shift(Component.literal("- " + bonus.toString()));
                }
            }
            smartTooltip.into(tooltipComponents);
        }
    }


}
