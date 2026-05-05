package net.maven.malady.currency;

import net.maven.malady.currency.commands.CurrencyCommands;
import net.maven.malady.currency.data.CurrencyAttachments;
import net.maven.malady.currency.items.CurrencyItems;
import net.maven.malady.currency.network.SyncCurrencyPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Currency {

    public static void init(IEventBus modEventBus, ModContainer modContainer) {

        // Register module items
        registerItems(modEventBus);

        // Register networking code
        registerNetworking(modEventBus);

    }

    // Register module items
    public static void registerItems(IEventBus modEventBus) {
        CurrencyItems.ITEMS.register(modEventBus);
    }

    // Sets up an registers all network-related code
    public static void registerNetworking(IEventBus modEventBus) {
        CurrencyAttachments.ATTACHMENT_TYPES.register(modEventBus); // Main attachment registration

        modEventBus.addListener(Currency::registerPackets); // Register all packets

    }

    private static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        // Register the packet as PLAY_TO_CLIENT (server -> client)
        registrar.playToClient(
                SyncCurrencyPacket.TYPE,
                SyncCurrencyPacket.STREAM_CODEC,
                SyncCurrencyPacket::handle
        );
    }


}
