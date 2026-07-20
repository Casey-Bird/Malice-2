package net.maven.malady.mixins.bonded;

import com.google.common.collect.Multimap;
import com.iamkaf.amber.api.inventory.ItemHelper;
import com.iamkaf.bonded.Bonded;
import com.iamkaf.bonded.api.event.BondEvent;
import com.iamkaf.bonded.component.ItemLevelContainer;
import com.iamkaf.bonded.leveling.GameplayHooks;
import com.iamkaf.bonded.leveling.levelers.GearTypeLeveler;
import com.iamkaf.bonded.leveling.levelers.MeleeWeaponsLeveler;
import com.iamkaf.bonded.registry.DataComponents;
import com.iamkaf.bonded.registry.Sounds;
import com.iamkaf.bonded.util.ItemUtils;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import net.maven.malady.Config;
import net.maven.malady.Malady;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import oshi.util.tuples.Triplet;

import java.lang.ref.Reference;
import java.util.*;
import java.util.stream.Collectors;

@Mixin(GameplayHooks.class)
public class GameplayHookMixin {

    /**
     * @author Maven
     * @reason The event that gets called when an item "levels up"
     */
    @Overwrite
    private static void onItemLeveledUp(ItemStack stack, Player player, ItemLevelContainer container, Integer integer) {
        int itemLevel = ((ItemLevelContainer)stack.get((DataComponentType) DataComponents.ITEM_LEVEL_CONTAINER.get())).getLevel();
        Level level = player.level();
        Integer maxLevel = (Integer) Bonded.CONFIG.levelsToUpgrade.get();
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), itemLevel == maxLevel ? (SoundEvent) Sounds.ITEM_MAX_LEVEL.get() : (SoundEvent)Sounds.ITEM_LEVEL.get(), SoundSource.PLAYERS);
        if ((Boolean)Bonded.CONFIG.sendChatMessages.get()) {
            MutableComponent message = itemLevel == maxLevel ? Component.translatable("bonded.gameplay.max_level", new Object[]{stack.getDisplayName().getString(), itemLevel}) : Component.translatable("bonded.gameplay.level_up", new Object[]{stack.getDisplayName().getString(), itemLevel});
            player.sendSystemMessage(message.append(Component.literal(String.valueOf(itemLevel)).withStyle(ChatFormatting.GOLD)));
        }
    }

    /**
     * @author Maven
     * @reason New logic implemented
     */
    @Overwrite
    private static void processPlayerDealtDamage(Player player, DamageSource source, float amount) {

        ItemStack handItem = source.getWeaponItem();

        if (handItem == null) {
            handItem = ItemUtils.checkForRocketCrossbow(player, source);
        }

        if (handItem != null && !handItem.isEmpty()) {
            GearTypeLeveler leveler = Bonded.GEAR.getLeveler(handItem);
            boolean isMeleeWeapon = leveler instanceof MeleeWeaponsLeveler;
            if (isMeleeWeapon) {
                malady$emitProgressEvents(handItem, player, (int)((double)amount * (Double)Bonded.CONFIG.weaponDamageDealtExperienceGainedMultiplier.get()));
            }

            boolean isRangedWeapon = handItem.getItem() instanceof ProjectileWeaponItem;
            boolean isProjectile = source.getDirectEntity() instanceof Projectile;
            if (isRangedWeapon && isProjectile) {
                ItemStack foundBow = ItemUtils.tryToFindStack(player, handItem);
                malady$emitProgressEvents(foundBow, player, (int)((double)amount * (Double)Bonded.CONFIG.weaponDamageDealtExperienceGainedMultiplier.get()));
            }

            for(ItemStack slot : player.getArmorSlots()) {
                if (Bonded.GEAR.isGear(slot)) {
                    malady$emitProgressEvents(slot, player, (int)((double)amount * (Double)Bonded.CONFIG.weaponDamageDealtExperienceGainedMultiplier.get() + (double)1.0F));
                }
            }
        }
    }

    /**
     * @author Maven
     * @reason New logic
     */
    @Overwrite
    private static EventResult onEntityHurt(LivingEntity entity, DamageSource source, float amount) {
        Level level = entity.level();
        if (level.isClientSide) {
            return EventResult.pass();
        } else {
            Entity var5 = source.getEntity();
            if (var5 instanceof Player) {
                ResourceLocation entityId = EntityType.getKey(entity.getType());
                Set<EntityType> double_multiplier_mob_list = Config.doubled_multiplier_mobs;
                Set<EntityType> blacklisted_mob_list = Config.blacklisted_bonded_mobs;

                int multiplier = 1;
                for (EntityType this_entity_type : blacklisted_mob_list) {
                    ResourceLocation this_entity_id = EntityType.getKey(this_entity_type);
                    if (this_entity_id.equals(entityId)) {
                        multiplier = 0;
                    }
                }

                for (EntityType double_entity_type : double_multiplier_mob_list) {
                    ResourceLocation double_entity_id = EntityType.getKey(double_entity_type);
                    if (double_entity_id.equals(entityId)) {
                        multiplier = 2;
                    }
                }

                Player player = (Player)var5;
                processPlayerDealtDamage(player, source, multiplier);
            }

            if (entity instanceof Player) {
                Player player = (Player)entity;
                malady$processPlayerTakenDamage(player, source, amount);
            }

            return EventResult.pass();
        }
    }

    @Unique
    private static void malady$emitProgressEvents(ItemStack item, Player player, int experienceAmount) {
        if (experienceAmount < 0) { return; }

        if (!item.isEmpty()) {
            ItemStack gear = Bonded.GEAR.initComponent(item);
            ItemLevelContainer container = (ItemLevelContainer)gear.get((DataComponentType)DataComponents.ITEM_LEVEL_CONTAINER.get());

            if (container == null) {
                return;
            }

            int currentWeaponBond = container.getBond();

            CompoundEventResult<Integer> result = ((BondEvent.ExperienceGained)BondEvent.ITEM_EXPERIENCE_GAINED.invoker()).experience(gear, player, container, experienceAmount);
            if (!result.interruptsFurtherEvaluation()) {
                int newExperienceAmount = result.object() != null ? (Integer)result.object() : experienceAmount;
                boolean hasLeveled = Bonded.GEAR.giveItemExperience(gear, newExperienceAmount);

                // TODO This will crash us if the item gets mutated here because the item will be destroyed essentially
                malady$checkMutation(item, player);

                assert item != null;

                // TODO This will crash us if the item gets mutated here because the item will be destroyed essentially
                //malady$checkAndApplyBonus(item, newExperienceAmount);

            }
        }
    }

    @Unique
    private static void malady$processPlayerTakenDamage(Player player, DamageSource source, float amount) {
        Iterable<ItemStack> playerArmorSlots = player.getArmorSlots();
        if (source.getEntity() != null) {
            for(ItemStack slot : playerArmorSlots) {
                if (Bonded.GEAR.isGear(slot)) {
                    malady$emitProgressEvents(slot, player, ((Double)Bonded.CONFIG.armorDamageTakenExperienceGainedMultiplier.get()).intValue());
                }
            }
        }
    }

    @Unique
    private static void malady$checkMutation(ItemStack item, Player player) {
        ItemStack gear = Bonded.GEAR.initComponent(item);
        ItemLevelContainer container = (ItemLevelContainer) gear.get((DataComponentType) DataComponents.ITEM_LEVEL_CONTAINER.get());
        assert container != null;

        int currentWeaponBond = container.getBond();

        for (Triplet<String, Integer, String> triplet : Config.mutationConfig) {
            String sourceItemId = triplet.getA();
            Integer requiredBond = triplet.getB();
            String targetItemId = triplet.getC();

            if (currentWeaponBond >= requiredBond) {
                if (malady$isItemMatching(item, sourceItemId)) {
                    malady$replaceItemInHotbar(player, sourceItemId, targetItemId, item);
                    break;
                }
            }
        }
    }

    @Unique
    private static boolean malady$isItemMatching(ItemStack itemStack, String itemId) {
        ResourceLocation itemLocation = ResourceLocation.parse(itemId);
        return BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(itemLocation);
    }

    @Unique
    private static void malady$replaceItemInHotbar(Player player, String sourceItemId, String targetItemId, ItemStack currentItem) {
        ResourceLocation targetLocation = ResourceLocation.parse(targetItemId);
        Item targetItem = BuiltInRegistries.ITEM.get(targetLocation);

        if (targetItem == null) {
            return;
        }

        ItemStack newItemStack = new ItemStack(targetItem);
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarStack = player.getInventory().getItem(i);
            if (malady$isItemMatching(hotbarStack, sourceItemId) && hotbarStack == currentItem) {
                player.getInventory().setItem(i, newItemStack);
                break;
            }
        }
    }

    @Unique
    private static void malady$checkAndApplyBonus(ItemStack item, int bond) {
        if (item.isEmpty()) return;

        // Ensure armor has attributes before checking
        if (malady$isArmorItem(item)) {
            malady$ensureArmorHasAttributes(item);
        }

        ItemAttributeModifiers attributeModifiers = item.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
        if (attributeModifiers == null || attributeModifiers.modifiers().isEmpty()) {
            return;
        }

        // Map to track ALL bond bonuses for ALL attributes
        Map<Holder<Attribute>, Double> allBondBonuses = new HashMap<>();
        Map<Holder<Attribute>, EquipmentSlotGroup> attributeSlots = new HashMap<>();

        // Track which bond bonus IDs we're using to avoid duplicates
        Set<ResourceLocation> usedBondIds = new HashSet<>();

        // Collect ALL existing modifiers and identify bond bonuses
        List<ItemAttributeModifiers.Entry> nonBondModifiers = new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : attributeModifiers.modifiers()) {
            ResourceLocation modId = entry.modifier().id();

            // Check if it's a bond bonus (our mod + starts with "bond_bonus_")
            if (modId.getNamespace().equals(Malady.MODID) && modId.getPath().startsWith("bond_bonus_")) {
                // Track this bond bonus
                Holder<Attribute> attribute = entry.attribute();
                double currentBonus = allBondBonuses.getOrDefault(attribute, 0.0);
                allBondBonuses.put(attribute, currentBonus + entry.modifier().amount());
                attributeSlots.put(attribute, entry.slot());
                usedBondIds.add(modId);
            } else {
                // Keep all non-bond modifiers (including modded attributes!)
                nonBondModifiers.add(entry);
            }
        }

        // Get ALL attributes from the item (including modded ones)
        List<Holder<Attribute>> allAttributes = new ArrayList<>();
        for (ItemAttributeModifiers.Entry entry : attributeModifiers.modifiers()) {
            if (!allAttributes.contains(entry.attribute())) {
                allAttributes.add(entry.attribute());
            }
        }

        if (allAttributes.isEmpty()) return;

        // Pick a random attribute from ALL attributes (including modded ones)
        Random random = new Random();
        Holder<Attribute> selectedAttribute = allAttributes.get(random.nextInt(allAttributes.size()));

        // Generate random increase (0.01 to 0.05)
        random = new Random();
        int random_increase = random.nextInt(5) + 1; // 1-5
        float random_additional = random_increase / 100.0f;

        // Update the bond bonus for the selected attribute
        double currentBonusForSelected = allBondBonuses.getOrDefault(selectedAttribute, 0.0);
        double newBonusForSelected = currentBonusForSelected + random_additional;
        allBondBonuses.put(selectedAttribute, newBonusForSelected);

        // Ensure slot is stored for selected attribute
        if (!attributeSlots.containsKey(selectedAttribute)) {
            for (ItemAttributeModifiers.Entry entry : attributeModifiers.modifiers()) {
                if (entry.attribute().equals(selectedAttribute)) {
                    attributeSlots.put(selectedAttribute, entry.slot());
                    break;
                }
            }
        }

        // Build new modifiers
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        // First, add ALL non-bond modifiers (preserves vanilla and modded attributes)
        for (ItemAttributeModifiers.Entry entry : nonBondModifiers) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }

        // Then add ALL bond bonuses
        for (Map.Entry<Holder<Attribute>, Double> entry : allBondBonuses.entrySet()) {
            if (entry.getValue() > 0) {
                Holder<Attribute> attribute = entry.getKey();
                double totalBonus = entry.getValue();

                // Create a unique ID for this attribute's bond bonus
                String attrPath = attribute.unwrapKey()
                        .map(key -> key.location().getPath().replace('.', '_'))
                        .orElse("unknown_" + UUID.randomUUID());

                // Include the bond level in the ID to ensure uniqueness
                ResourceLocation bonusId = ResourceLocation.fromNamespaceAndPath(
                        Malady.MODID,
                        "bond_bonus_" + attrPath + "_" + bond
                );

                // If this ID was already used, append a random number
                if (usedBondIds.contains(bonusId)) {
                    bonusId = ResourceLocation.fromNamespaceAndPath(
                            Malady.MODID,
                            "bond_bonus_" + attrPath + "_" + bond + "_" + random.nextInt(1000)
                    );
                }

                EquipmentSlotGroup slot = attributeSlots.getOrDefault(attribute, EquipmentSlotGroup.ANY);
                builder.add(attribute, new AttributeModifier(bonusId, totalBonus, AttributeModifier.Operation.ADD_VALUE), slot);
            }
        }

        // Apply to item
        item.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
    }

    @Unique
    private static List<Holder<Attribute>> malady$getSuitableAttributes(ItemAttributeModifiers modifiers) {
        // Just return all attributes - we want to include modded ones!
        List<Holder<Attribute>> allAttributes = new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (!allAttributes.contains(entry.attribute())) {
                allAttributes.add(entry.attribute());
            }
        }

        return allAttributes;
    }

    @Unique
    private static boolean isBadAttribute(String path) {
        // Only filter out truly problematic attributes
        return path.contains("gravity") ||
                path.contains("scale") ||
                path.contains("sprinting") ||
                path.contains("flying") ||
                path.contains("step_height_add") ||
                path.contains("fall_damage_multiplier") ||
                path.contains("explosion_damage_reduction") ||
                path.contains("water_movement") ||
                path.contains("lava_movement") ||
                path.contains("jump_strength") || // Usually problematic
                path.contains("safe_fall_distance");
    }

    @Unique
    private static boolean malady$isArmorItem(ItemStack item) {
        if (item.isEmpty()) return false;

        // Check by item class
        Item itemType = item.getItem();
        if (itemType instanceof ArmorItem) return true;

        // Check by name patterns
        String name = itemType.toString().toLowerCase();
        return name.contains("helmet") ||
                name.contains("chestplate") ||
                name.contains("leggings") ||
                name.contains("boots") ||
                name.contains("armor") ||
                name.contains("cuirass") ||
                name.contains("greaves") ||
                name.contains("pauldron") ||
                name.contains("gauntlet");
    }
    @Unique
    private static void malady$ensureArmorHasAttributes(ItemStack armor) {
        if (armor.isEmpty()) return;

        ItemAttributeModifiers existing = armor.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);

        // Check if armor already has any suitable attributes
        boolean hasSuitableAttributes = false;
        if (existing != null && !existing.modifiers().isEmpty()) {
            List<Holder<Attribute>> suitable = malady$getSuitableAttributes(existing);
            hasSuitableAttributes = !suitable.isEmpty();
        }

        if (!hasSuitableAttributes) {

            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            if (existing != null) {
                // Copy existing modifiers
                for (ItemAttributeModifiers.Entry entry : existing.modifiers()) {
                    builder.add(entry.attribute(), entry.modifier(), entry.slot());
                }
            }

            // Determine armor slot
            EquipmentSlotGroup slotGroup = malady$getArmorSlotGroup(armor);

            // Try to add vanilla armor attribute - NO CAST NEEDED
            Optional<Holder.Reference<Attribute>> armorRef = BuiltInRegistries.ATTRIBUTE
                    .getHolder(ResourceLocation.parse("minecraft:generic.armor"));

            if (armorRef.isPresent()) {
                double baseArmorValue = malady$getBaseArmorValue(armor);

                ResourceLocation armorId = ResourceLocation.fromNamespaceAndPath(
                        Malady.MODID,
                        "base_armor_value"
                );

                AttributeModifier armorModifier = new AttributeModifier(
                        armorId,
                        baseArmorValue,
                        AttributeModifier.Operation.ADD_VALUE
                );

                // Reference<Attribute> is already a Holder<Attribute>
                builder.add(armorRef.get(), armorModifier, slotGroup);
            }

            // Try to add armor toughness - NO CAST NEEDED
            Optional<Holder.Reference<Attribute>> toughnessRef = BuiltInRegistries.ATTRIBUTE
                    .getHolder(ResourceLocation.parse("minecraft:generic.armor_toughness"));

            if (toughnessRef.isPresent()) {
                double toughnessValue = malady$getArmorToughness(armor);
                if (toughnessValue > 0) {
                    ResourceLocation toughnessId = ResourceLocation.fromNamespaceAndPath(
                            Malady.MODID,
                            "base_armor_toughness"
                    );

                    AttributeModifier toughnessModifier = new AttributeModifier(
                            toughnessId,
                            toughnessValue,
                            AttributeModifier.Operation.ADD_VALUE
                    );

                    // Reference<Attribute> is already a Holder<Attribute>
                    builder.add(toughnessRef.get(), toughnessModifier, slotGroup);
                }
            }

            // Apply updated modifiers
            armor.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
        }
    }

    @Unique
    private static EquipmentSlotGroup malady$getArmorSlotGroup(ItemStack armor) {
        Item item = armor.getItem();

        // Check for ArmorItem class first
        if (item instanceof ArmorItem armorItem) {
            return switch (armorItem.getEquipmentSlot()) {
                case HEAD -> EquipmentSlotGroup.HEAD;
                case CHEST -> EquipmentSlotGroup.CHEST;
                case LEGS -> EquipmentSlotGroup.LEGS;
                case FEET -> EquipmentSlotGroup.FEET;
                default -> EquipmentSlotGroup.ANY;
            };
        }

        // Fallback: check by name
        String name = item.toString().toLowerCase();
        if (name.contains("helmet") || name.contains("mask") || name.contains("hat"))
            return EquipmentSlotGroup.HEAD;
        if (name.contains("chestplate") || name.contains("cuirass") || name.contains("vest"))
            return EquipmentSlotGroup.CHEST;
        if (name.contains("leggings") || name.contains("greaves") || name.contains("pants"))
            return EquipmentSlotGroup.LEGS;
        if (name.contains("boots") || name.contains("shoes") || name.contains("footwear"))
            return EquipmentSlotGroup.FEET;

        return EquipmentSlotGroup.ANY;
    }

    @Unique
    private static double malady$getBaseArmorValue(ItemStack armor) {
        if (armor.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getDefense();
        }

        // Fallback for modded armor
        String name = armor.getItem().toString().toLowerCase();
        if (name.contains("leather")) return 1.0;
        if (name.contains("chainmail")) return 2.0;
        if (name.contains("iron")) return 3.0;
        if (name.contains("diamond")) return 4.0;
        if (name.contains("netherite")) return 4.0;
        if (name.contains("gold")) return 2.0;
        if (name.contains("turtle")) return 2.0;
        return 1.0; // Default for unknown armor
    }

    @Unique
    private static double malady$getArmorToughness(ItemStack armor) {
        if (armor.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getToughness();
        }

        // Fallback for modded armor
        String name = armor.getItem().toString().toLowerCase();
        if (name.contains("diamond")) return 2.0;
        if (name.contains("netherite")) return 3.0;
        return 0.0; // No toughness for others
    }
}