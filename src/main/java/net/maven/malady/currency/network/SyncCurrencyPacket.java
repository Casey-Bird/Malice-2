package net.maven.malady.currency.network;

import net.maven.malady.Malady;
import net.maven.malady.currency.data.CurrencyData;
import net.maven.malady.statistics.client.PlayerHUDOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;


public record SyncCurrencyPacket(int currency, UUID playerId) implements CustomPacketPayload {
    public static final Type<SyncCurrencyPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Malady.MODID, "sync_currency"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCurrencyPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.currency);
                buf.writeUUID(packet.playerId);
            },
            buf -> new SyncCurrencyPacket(buf.readInt(), buf.readUUID())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncCurrencyPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Client-side handling
            if (context.flow().isClientbound()) {
                assert Minecraft.getInstance().level != null;
                Player player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerId);
                if (player != null) {
                    CurrencyData data = CurrencyData.get(player);
                    if (data != null) {
                        data.setCurrency(packet.currency);
                        PlayerHUDOverlay.currencyAmount = packet.currency();
                    }
                }
            }
        });
    }
}