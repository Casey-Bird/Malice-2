package net.maven.malady.statistics;

import dev.architectury.registry.registries.DeferredRegister;
import net.maven.malady.Malady;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.maven.malady.statistics.data.TiredData;
import net.maven.malady.statistics.events.server.SleepEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class Statistics {


    public static void init(IEventBus eventBus) {
        StatisticAttachments.ATTACHMENT_TYPES.register(eventBus);
    }


    public static void registerEvents() {

    }



    public static void registerDataAttachments() {



    }

}
