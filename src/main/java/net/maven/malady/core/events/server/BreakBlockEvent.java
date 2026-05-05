package net.maven.malady.core.events.server;

import net.maven.malady.Config;
import net.maven.malady.core.items.CoreItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BreakBlockEvent {

    public static void register(IEventBus eventBus) {

        NeoForge.EVENT_BUS.addListener(BreakBlockEvent::onBlockBreak);
    }

    private static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        // Only run on server side and if not creative mode
        if (level.isClientSide()) {
            return;
        }

        // Check if the broken block is a plant
        if (isPlant(state)) {

        }
    }

    private static boolean isPlant(BlockState state) {
        Block block = state.getBlock();

        boolean contains_block = Config.plant_fiber_plants.contains(block);

        return contains_block;
    }
}