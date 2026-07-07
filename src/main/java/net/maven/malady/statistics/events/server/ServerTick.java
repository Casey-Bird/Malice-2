package net.maven.malady.statistics.events.server;

import net.maven.malady.Malady;
import net.maven.malady.statistics.api.MoodAPI;
import net.maven.malady.statistics.data.MoodData;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Malady.MODID)
public class ServerTick {


    private static int tickCounter = 0;
    private static final int ONE_MINUTE_TICKS = 20 * 60; // 1200


    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;
        if (tickCounter >= ONE_MINUTE_TICKS) {
            tickCounter = 0;
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                MoodAPI.addSadness(player);
            }
        }
    }
}
