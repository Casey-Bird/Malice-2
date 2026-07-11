package net.maven.malady.statistics.events.server;


import net.maven.malady.Malady;
import net.maven.malady.statistics.api.MoodAPI;
import net.maven.malady.statistics.data.MoodData;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Malady.MODID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            MoodData current = player.getData(StatisticAttachments.MOOD_DATA);
            if (current.moodAmount() == 0) {
                player.setData(StatisticAttachments.MOOD_DATA, new MoodData(500));
            }
            MoodAPI.syncMoodToClient(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer && event.getOriginal() instanceof ServerPlayer oldPlayer) {
            MoodData oldData = oldPlayer.getData(StatisticAttachments.MOOD_DATA);
            newPlayer.setData(StatisticAttachments.MOOD_DATA, oldData);
        }
    }

}
