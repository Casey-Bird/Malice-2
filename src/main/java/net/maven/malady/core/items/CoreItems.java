package net.maven.malady.core.items;

import net.maven.malady.Malady;
import net.maven.malady.core.blocks.nethergateway.NetherGatewayBlock;
import net.maven.malady.core.items.soul.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CoreItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Malady.MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Malady.MODID);

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Malady.MODID);

    public static final Supplier<Block> NETHER_GATEWAY_BLOCK = BLOCKS.register(
            "nether_gateway_block",
            () -> new NetherGatewayBlock(Block.Properties.of()
                    .strength(4.0f, 6.0f)          // adjust as needed
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops() // optional
            )
    );

    public static final Supplier<Item> NETHER_GATEWAY_BLOCK_ITEM = ITEMS.register(
            "nether_gateway_block",
            () -> new BlockItem(NETHER_GATEWAY_BLOCK.get(), new Item.Properties())
    );

    public static final Supplier<Item> LESSER_SOUL_CRYSTAL = ITEMS.register(
            "lesser_soul_vial",
            LesserSoulCrystal::new
    );

    public static final Supplier<Item> UNDEAD_SOUL_CRYSTAL = ITEMS.register(
            "undead_soul_vial",
            UndeadSoulCrystal::new
    );


    public static final Supplier<Item> BRIMSTONE_SOUL_CRYSTAL = ITEMS.register(
            "brimstone_soul_vial",
            BrimstoneSoulCrystal::new
    );

    public static final Supplier<Item> INFERNAL_SOUL_CRYSTAL = ITEMS.register(
            "infernal_soul_vial",
            InfernalSoulCrystal::new
    );

    public static final Supplier<Item> FROSTED_SOUL_CRYSTAL = ITEMS.register(
            "frosted_soul_vial",
            FrostedSoulCrystal::new
    );

    public static final Supplier<Item> KRAKEN_SOUL_CRYSTAL = ITEMS.register(
            "kraken_soul_vial",
            KrakenSoulCrystal::new
    );

    public static final Supplier<Item> MALIGNANT_SOUL_CRYSTAL = ITEMS.register(
            "malignant_soul_vial",
            MalignantSoulCrystal::new
    );




    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MALADY_TAB =
            CREATIVE_TABS.register("malady_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creative_tab.malady.malady_tab"))
                    .icon(() -> new ItemStack(FROSTED_SOUL_CRYSTAL.get()))
                    .displayItems((parameters, output) -> {

                        output.accept(NETHER_GATEWAY_BLOCK_ITEM.get());

                        output.accept(LESSER_SOUL_CRYSTAL.get());
                        output.accept(UNDEAD_SOUL_CRYSTAL.get());
                        output.accept(BRIMSTONE_SOUL_CRYSTAL.get());
                        output.accept(INFERNAL_SOUL_CRYSTAL.get());
                        output.accept(FROSTED_SOUL_CRYSTAL.get());
                        output.accept(KRAKEN_SOUL_CRYSTAL.get());
                        output.accept(MALIGNANT_SOUL_CRYSTAL.get());
                    })
                    .build());



}
