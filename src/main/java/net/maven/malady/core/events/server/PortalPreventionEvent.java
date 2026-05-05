package net.maven.malady.core.events.server;


import net.maven.malady.Malady;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Malady.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PortalPreventionEvent {

    @SubscribeEvent
    public static void onPortalForm(BlockEvent.PortalSpawnEvent event) {
        // Cancel ALL portal creation
        event.setCanceled(true);
    }


}
