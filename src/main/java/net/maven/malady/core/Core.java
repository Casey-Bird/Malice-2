package net.maven.malady.core;

import net.maven.malady.core.blocks.CoreBlocks;
import net.maven.malady.core.events.server.BreakBlockEvent;
import net.maven.malady.core.items.CoreItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

public class Core {


    public static void init(IEventBus modEventBus, ModContainer modContainer) {


        // Initialize all core Malady items
        registerItems(modEventBus);

        // Registers all events
        registerEvents(modEventBus);

        // Registers all blocks
        registerBlocks(modEventBus);

    }

    public static void registerItems(IEventBus modEventBus) {
        CoreItems.ITEMS.register(modEventBus);
    }

    public static void registerEvents(IEventBus modEventBus) {
        BreakBlockEvent.register(modEventBus);
    }

    public static void registerBlocks(IEventBus modEventBus) {

        CoreBlocks.register(modEventBus);

    }


}
