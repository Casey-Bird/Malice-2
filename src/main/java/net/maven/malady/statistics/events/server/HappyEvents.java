package net.maven.malady.statistics.events.server;


import net.maven.malady.Malady;
import net.maven.malady.statistics.api.MoodAPI;
import net.maven.malady.statistics.data.MoodData;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Malady.MODID)
public class HappyEvents {


    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return; // only server

        Block placedBlock = event.getState().getBlock();

        if (placedBlock.builtInRegistryHolder().is(BlockTags.FLOWERS)) {
            MoodAPI.addHappiness((ServerPlayer) player, 1);

        }
    }


}
