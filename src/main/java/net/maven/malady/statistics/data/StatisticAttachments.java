package net.maven.malady.statistics.data;

import net.maven.malady.Malady;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class StatisticAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Malady.MODID);

    // The attachment itself – default value = MoodData(0), saved with your CODEC
    public static final Supplier<AttachmentType<MoodData>> MOOD_DATA =
            ATTACHMENT_TYPES.register(
                    "mood_data",
                    () -> AttachmentType.<MoodData>builder(() -> new MoodData(0))
                            .serialize(MoodData.CODEC)   // automatic NBT save/load
                            .build()
            );


}
