package net.maven.malady.statistics.data;

import net.maven.malady.Malady;
import net.maven.malady.statistics.data.MoodData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record MoodSyncPayload(UUID playerId, MoodData moodData) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, UUID> UUID_CODEC =
            StreamCodec.of(
                    (buf, uuid) -> buf.writeUUID(uuid),
                    buf -> buf.readUUID()
            );


    public static final Type<MoodSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Malady.MODID, "mood_sync")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MoodSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    UUID_CODEC, MoodSyncPayload::playerId,
                    MoodData.STREAM_CODEC, MoodSyncPayload::moodData,
                    MoodSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}