package net.maven.malady.statistics.events.client;


import net.maven.malady.Malady;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.GuiLayerManager;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Malady.MODID, value = Dist.CLIENT)
public class HideVanillaHUD {

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {

        ResourceLocation layerName = event.getName();

        // Check which layer is being rendered
        if ( layerName.equals(VanillaGuiLayers.PLAYER_HEALTH) ) {
            event.setCanceled(true);
        }

        if ( layerName.equals(VanillaGuiLayers.FOOD_LEVEL) ) {
            event.setCanceled(true);
        }


    }

}
