package net.maven.malady.statistics.events.server;


import net.maven.malady.Malady;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EventBusSubscriber(modid = Malady.MODID)
public class SleepEvent {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        // Check if the player is currently sleeping in a bed
        if (event.getEntity().isSleeping()) {



            System.out.println("Player is asleep now");
            // --- Your custom logic for when the player is sleeping goes here ---
            // For example, increase a "dream power" counter by 1 each tick.
            // int currentDreamPower = event.getEntity().getPersistentData().getInt("dream_power");
            // event.getEntity().getPersistentData().putInt("dream_power", currentDreamPower + 1);
        }
    }

    // Allow sleep any time
    @SubscribeEvent
    public static void onSleepTimeCheck(CanPlayerSleepEvent event) {
        event.setProblem(null);
    }


    // Prevent the night from skipping when players wake up
    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        event.setTimeAddition(0);
    }


    @SubscribeEvent
    public static void onSleepContinue(CanContinueSleepingEvent event) {
        event.setContinueSleeping(true);
    }


    @SubscribeEvent
    public static void onPlayerSetSpawnEvent(PlayerSetSpawnEvent event) {
        // event.setCanceled(true);

        // TODO Allow certain blocks to set spawn points


    }

}
