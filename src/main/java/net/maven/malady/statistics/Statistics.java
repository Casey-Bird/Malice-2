package net.maven.malady.statistics;

import net.maven.malady.Malady;
import net.maven.malady.statistics.data.ClientMoodCache;
import net.maven.malady.statistics.data.MoodSyncPayload;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@EventBusSubscriber(modid = Malady.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Statistics {


    public static void init(IEventBus eventBus) {
        StatisticAttachments.ATTACHMENT_TYPES.register(eventBus);
    }


    public static void registerEvents() {

    }


    public static void registerDataAttachments() {

    }

    public static void registerEffects () {


    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Malady.MODID)
                .versioned("1.0")
                .optional();

        registrar.playBidirectional(
                MoodSyncPayload.TYPE,
                MoodSyncPayload.STREAM_CODEC,
                (payload, context) -> {

                    if (context.flow().isClientbound()) {
                        context.enqueueWork(() -> {

                            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                            if (mc.player != null && mc.player.getUUID().equals(payload.playerId())) {
                                ClientMoodCache.update(payload.playerId(), payload.moodData());
                            } else {
                                ClientMoodCache.update(payload.playerId(), payload.moodData());
                            }
                        });
                    }
                }
        );
    }

}
