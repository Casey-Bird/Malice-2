package net.maven.malady.currency.api;

import dev.architectury.event.events.common.TickEvent;
import net.maven.malady.currency.data.CurrencyData;
import net.maven.malady.currency.network.SyncCurrencyPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class CurrencyAPI {

    /*
        Adds currency to a given player
    */
    public static void addCurrency(Player player, int amount) {

        return;

//        CurrencyData data = CurrencyData.get(player);
//
//        int current_currency = data.getCurrency();
//        System.out.println(current_currency);
//
//        if (data != null) {
//            data.addCurrency(amount);
//
//            if (player instanceof ServerPlayer serverPlayer) {
//                syncCurrency(serverPlayer);
//            }
//
//        }

    }

    /*
        Removes currency from a player and synchronizes it to the client
     */
    public static void removeCurrency(Player player, int amount) {
        CurrencyData data = CurrencyData.get(player);

        if (data == null) {
            return;
        }

        data.removeCurrency(amount);

        if (player instanceof ServerPlayer serverPlayer) {
            syncCurrency(serverPlayer);
        }


    }

    public static void setCurrency(Player player, int amount) {
        CurrencyData data = CurrencyData.get(player);

        if (data == null) {
            return;
        }

        data.setCurrency(amount);

        if (player instanceof ServerPlayer serverPlayer) {
            syncCurrency(serverPlayer);
        }
    }



    // Syncs the packet to the client
    private static void syncCurrency(ServerPlayer player) {
        CurrencyData data = CurrencyData.get(player);
        if (data != null) {
            SyncCurrencyPacket packet = new SyncCurrencyPacket(data.getCurrency(), player.getUUID());
            PacketDistributor.sendToPlayer(player, packet);
        }
    }

}
