package net.maven.malady.statistics.events.server;


import net.maven.malady.Malady;
import net.maven.malady.statistics.api.MoodAPI;
import net.maven.malady.statistics.data.MoodData;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Malady.MODID)
public class HappyEvents {


    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.level().isClientSide()) return; // only server

        Block placedBlock = event.getState().getBlock();

        if (placedBlock.builtInRegistryHolder().is(BlockTags.FLOWERS)) {
            MoodAPI.addHappiness((ServerPlayer) player, 30);

        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // Only run on server
        if (event.getLevel().isClientSide()) return;

        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Check if the target is an animal
        if (!(event.getTarget() instanceof Animal animal)) return;

        // Get the item the player is holding
        ItemStack held = event.getItemStack();
        if (held.isEmpty()) return;

        // Check if the animal accepts this item as food
        if (!animal.isFood(held)) return;

        // If we reached this point, the player just fed the animal
        MoodAPI.addHappiness(serverPlayer, 100);
    }


}
