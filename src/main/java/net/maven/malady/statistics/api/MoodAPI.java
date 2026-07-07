package net.maven.malady.statistics.api;

import net.maven.malady.statistics.data.MoodData;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.server.level.ServerPlayer;

public class MoodAPI {



    public static void addHappiness(ServerPlayer player, int amount) {
        MoodData current = player.getData(StatisticAttachments.MOOD_DATA.get());
        player.setData(StatisticAttachments.MOOD_DATA.get(), current.increment(amount));
    }


    public static void addSadness(ServerPlayer player) {
            MoodData current = player.getData(StatisticAttachments.MOOD_DATA.get());
            player.setData(StatisticAttachments.MOOD_DATA.get(), current.decrement(1));
    }


    public static int getHappiness(ServerPlayer player) {
        MoodData current = player.getData(StatisticAttachments.MOOD_DATA.get());
        return current.moodAmount();
    }

}
