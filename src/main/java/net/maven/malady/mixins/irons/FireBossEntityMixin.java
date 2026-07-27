package net.maven.malady.mixins.irons;


import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FireBossEntity.class)
public class FireBossEntityMixin {


    @Overwrite
    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, (double)20.0F)
                .add(AttributeRegistry.SPELL_POWER, (double)2.25F)
                .add(Attributes.ARMOR, (double)35.0F)
                .add(AttributeRegistry.SPELL_RESIST, (double)1.25F)
                .add(AttributeRegistry.FIRE_MAGIC_RESIST, (double)1.5F)
                .add(Attributes.MAX_HEALTH, (double)4000.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6)
                .add(Attributes.FOLLOW_RANGE, (double)48.0F)
                .add(Attributes.SCALE, (double)4.75F)
                .add(Attributes.GRAVITY, 0.03)
                .add(Attributes.ENTITY_INTERACTION_RANGE, (double)3.0F)
                .add(Attributes.STEP_HEIGHT, (double)4.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.31);
    }


}
