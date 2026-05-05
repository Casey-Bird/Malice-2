package net.maven.malady.core.blocks;

import net.maven.malady.Malady;
import net.maven.malady.core.blocks.nethergateway.NetherGatewayBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CoreBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Malady.MODID);
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Malady.MODID);

    // Register the Nether Gateway block
    public static final DeferredBlock<Block> NETHER_GATEWAY = BLOCKS.register(
            "nether_gateway",
            () -> new NetherGatewayBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(1.5f, 6.0f) // Softer than obsidian
                            .sound(SoundType.GLASS)
                            .lightLevel(state -> 7) // Glowing purple effect
                            .noOcclusion() // Allows you to see through it maybe?
            )
    );

    // Register the item for the block
    public static final DeferredItem<Item> NETHER_GATEWAY_ITEM = ITEMS.register(
            "nether_gateway",
            () -> new BlockItem(NETHER_GATEWAY.get(),
                    new Item.Properties()
                            .fireResistant()
            )
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

}

