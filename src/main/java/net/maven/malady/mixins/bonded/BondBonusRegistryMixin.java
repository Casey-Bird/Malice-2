package net.maven.malady.mixins.bonded;


import com.iamkaf.amber.api.inventory.ItemHelper;
import com.iamkaf.bonded.bonuses.BondBonus;
import com.iamkaf.bonded.component.ItemLevelContainer;
import com.iamkaf.bonded.leveling.levelers.GearTypeLeveler;
import com.iamkaf.bonded.registry.BondBonusRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(BondBonusRegistry.class)
public class BondBonusRegistryMixin {

    // This is the function that applies bonuses (attribute modifiers) to gear // Overwriting to do nothing because we will have our own bond bonus logic
    @Overwrite
    public void applyBonuses(ItemStack gear, GearTypeLeveler gearType, ItemLevelContainer container) {

    }


}
