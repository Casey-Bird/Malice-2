package net.maven.malady.statistics.api;

import net.maven.malady.statistics.data.MoodData;
import net.maven.malady.statistics.data.MoodSyncPayload;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;

public class MoodAPI {



    public static void addHappiness(ServerPlayer player, int amount) {
        MoodData current = player.getData(StatisticAttachments.MOOD_DATA.get());

        // hard coded limit
        if (current.moodAmount() >= 1000) {
            return;
        }
        player.setData(StatisticAttachments.MOOD_DATA.get(), current.increment(amount));

        syncMoodToClient(player);
    }


    public static void addSadness(ServerPlayer player, int amount) {
            MoodData current = player.getData(StatisticAttachments.MOOD_DATA.get());

            // Hard coded floor
            if (current.moodAmount() <= 0) {
                return;
            }
            player.setData(StatisticAttachments.MOOD_DATA.get(), current.decrement(amount));

            syncMoodToClient(player);
    }


    public static int getHappiness(ServerPlayer player, int amount) {
        MoodData current = player.getData(StatisticAttachments.MOOD_DATA.get());
        return current.moodAmount();
    }


    public static void syncMoodToClient(ServerPlayer player) {
        MoodData currentMood = player.getData(StatisticAttachments.MOOD_DATA);
        // If the mood hasn't been initialized, send a default (or initialize it first)
        if (currentMood == null) {
            currentMood = new MoodData(0);
            player.setData(StatisticAttachments.MOOD_DATA, currentMood);
        }
        player.connection.send(new ClientboundCustomPayloadPacket(
                new MoodSyncPayload(player.getUUID(), currentMood)
        ));
    }


}
