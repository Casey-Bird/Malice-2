package net.maven.malady.currency.events;

import net.maven.malady.Malady;
import net.maven.malady.currency.api.CurrencyAPI;
import net.maven.malady.currency.data.CurrencyData;
import net.maven.malady.currency.network.SyncCurrencyPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Malady.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CurrencyEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncCurrency((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncCurrency((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncCurrency((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();

        CurrencyData originalData = CurrencyData.get(original);
        CurrencyData newData = CurrencyData.get(newPlayer);

        if (originalData != null && newData != null) {
            var provider = newPlayer.level().registryAccess();
            CompoundTag nbt = originalData.serializeNBT(provider);
            newData.deserializeNBT(provider, nbt);
        }
    }

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {

    }

    public static void syncCurrency(ServerPlayer player) {
        CurrencyData data = CurrencyData.get(player);
        if (data != null) {
            SyncCurrencyPacket packet = new SyncCurrencyPacket(data.getCurrency(), player.getUUID());
            PacketDistributor.sendToPlayer(player, packet);
        }
    }


}
