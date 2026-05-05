package net.maven.malady;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import oshi.util.tuples.Triplet;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Malady.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
    //        .comment("Whether to log the dirt block on common setup")
    //        .define("logDirtBlock", true);

    //private static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
    //        .comment("A magic number")
    //        .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    //public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
    //        .comment("What you want the introduction message to be for the magic number")
    //        .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    //private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
    //        .comment("A list of items to log on common setup.")
    //        .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    // A list of items that are not allowed to level up in bonded levels
    private static final ModConfigSpec.ConfigValue<List<? extends String>> PLANT_FIBER_PLANTS = BUILDER
            .comment(" List of plants that are able to drop plant fibers.")
            .defineListAllowEmpty("plant_fiber_plants", List.of("minecraft:short_grass"), Config::validateBlockName);


    // A list of mobs that can drop 1 currency
    private static final ModConfigSpec.ConfigValue<List<? extends String>> CURRENCY_MOBS = BUILDER
            .comment(" A list of mobs that rubies can drop from after death.")
            .defineListAllowEmpty("currency_mobs", List.of(), Config::validateMobName);


    // A list of items that are not allowed to level up in bonded levels
    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BONDED_MOBS = BUILDER
            .comment(" A list of mobs .")
            .defineListAllowEmpty("blacklisted_bonded_mobs", List.of(), Config::validateMobName);

    // A list of mobs that can drop the undead soul crystal
    private static final ModConfigSpec.ConfigValue<List<? extends String>> UNDEAD_SOUL_CRYSTAL_MOBS = BUILDER
            .comment(" A list of mobs that the undead soul crystal can drop from.")
            .defineListAllowEmpty("undead_soul_crystal", List.of(), Config::validateMobName);

    // 2. Soul Crystal Drop Chance
    private static final ModConfigSpec.IntValue UNDEAD_SOUL_CRYSTAL_DROPCHANCE = BUILDER
            .comment("Drop chance percentage for undead soul crystals (0-1000): higher = easier")
            .defineInRange("undead_soul_crystal_dropchance",
                    100, // Default 100%
                    0,   // Minimum
                    100); // Maximum


    //
    private static final ModConfigSpec.ConfigValue<List<? extends String>> DOUBLE_MULTIPLIER_MOBS = BUILDER
            .comment(" A list of mobs that grant the player 2x bonded experience for dealing damage.")
            .defineListAllowEmpty("double_multiplier_bonded_mobs", List.of(), Config::validateMobName);


    private static final ModConfigSpec.ConfigValue<List<? extends List<?>>> CONFIG_LIST = BUILDER
            .comment("""
                 A list of configurations, each containing [string, number, string]
                 Example: [["minecraft:iron_sword", 50, "minecraft:diamond_sword"], ["minecraft:gold_sword", 1000, "minecraft:netherite_sword"]]""")
            .defineListAllowEmpty("mutation_config",
                    List.of(),
                    Config::validateNestedList);


    static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<String> undead_soul_crystal_mobs;
    public static Set<String> currency_mobs;
    public static int undead_soul_crystal_dropchance;
    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<EntityType> doubled_multiplier_mobs;
    public static Set<EntityType> blacklisted_bonded_mobs;
    public static List<Triplet<String, Integer, String>> mutationConfig;
    public static Set<Block> plant_fiber_plants;

    private static boolean validateMobName(final Object obj)
    {
        return obj instanceof String mobName && BuiltInRegistries.ENTITY_TYPE.containsKey(ResourceLocation.parse(mobName));
    }

    private static boolean validateBlockName(final Object obj)
    {
        return obj instanceof String blockName && BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(blockName));
    }

    // Validation for nested lists
    private static boolean validateNestedList(final Object obj)
    {
        if (!(obj instanceof List<?> nestedList)) {
            return false;
        }

        // Check if it has exactly 3 elements
        if (nestedList.size() != 3) {
            return false;
        }

        // Check types: [String, Number, String]
        return nestedList.get(0) instanceof String &&
                nestedList.get(1) instanceof Number &&
                nestedList.get(2) instanceof String;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        currency_mobs = new HashSet<>(CURRENCY_MOBS.get());

        undead_soul_crystal_mobs = new HashSet<>(UNDEAD_SOUL_CRYSTAL_MOBS.get());

        undead_soul_crystal_dropchance = UNDEAD_SOUL_CRYSTAL_DROPCHANCE.get();

        //logDirtBlock = LOG_DIRT_BLOCK.get();
        //magicNumber = MAGIC_NUMBER.get();
        //magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        plant_fiber_plants = PLANT_FIBER_PLANTS.get().stream()
                .map(blockName -> BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockName)))
                .collect(Collectors.toSet());

        // convert the list of strings into a set of items
        doubled_multiplier_mobs = DOUBLE_MULTIPLIER_MOBS.get().stream()
                .map(mobName -> BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(mobName)))
                .collect(Collectors.toSet());

        // convert the list of strings into a set of items
        blacklisted_bonded_mobs = BLACKLISTED_BONDED_MOBS.get().stream()
                .map(mobName -> BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(mobName)))
                .collect(Collectors.toSet());


        // Convert nested lists to Triplets for easier access
        mutationConfig = CONFIG_LIST.get().stream()
                .map(nestedList -> {
                    List<?> list = (List<?>) nestedList;
                    String first = (String) list.get(0);
                    Integer second = ((Number) list.get(1)).intValue();
                    String third = (String) list.get(2);
                    return new Triplet<>(first, second, third);
                })
                .collect(Collectors.toList());

    }
}
