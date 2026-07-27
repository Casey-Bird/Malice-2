package net.maven.malady.mixins.irons;

import io.redspace.ironsspellbooks.spells.fire.RaiseHellSpell;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RaiseHellSpell.class)
public class RaiseHellSpellMixin {

    @Overwrite
    private float getRadius(int spellLevel, LivingEntity entity) {
        return 24.0F;
    }


}
