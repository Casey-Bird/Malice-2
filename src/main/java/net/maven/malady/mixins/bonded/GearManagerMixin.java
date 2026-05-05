package net.maven.malady.mixins.bonded;

import com.iamkaf.bonded.Bonded;
import com.iamkaf.bonded.component.ItemLevelContainer;
import com.iamkaf.bonded.leveling.GearManager;
import com.iamkaf.bonded.leveling.levelers.GearTypeLeveler;
import com.iamkaf.bonded.registry.DataComponents;
import com.iamkaf.bonded.registry.GearTypeLevelerRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(GearManager.class)
public class GearManagerMixin {

    private static GearTypeLevelerRegistry gearTypeLevelerRegistry = new GearTypeLevelerRegistry();

    @Overwrite
    public boolean giveItemExperience(ItemStack item, int amount) {
        if (item != null && !item.isEmpty() && amount != 0) {
            DataComponentType<ItemLevelContainer> levelingComponent = (DataComponentType) DataComponents.ITEM_LEVEL_CONTAINER.get();
            if (!item.has(levelingComponent)) {
                item.set(levelingComponent, ItemLevelContainer.make(this.getMaxExperienceForItemType(item)));
            }

            ItemLevelContainer currentComponent = (ItemLevelContainer)item.get(levelingComponent);

            assert currentComponent != null;

            ItemLevelContainer newComponent = ((ItemLevelContainer) Objects.requireNonNull(currentComponent)).addExperience(amount).addBond(amount);

            item.set(levelingComponent, newComponent);

        }
        return false;
    }


    private int getMaxExperienceForItemType(ItemStack gear) {
        GearTypeLeveler leveler = this.getLeveler(gear);
        return leveler != null ? leveler.getMaxExperience(gear) : (Integer)Bonded.CONFIG.defaultMaxExperienceForUnknownItems.get();
    }

    public @Nullable GearTypeLeveler getLeveler(ItemStack gear) {
        return gearTypeLevelerRegistry.get(gear);
    }

}
