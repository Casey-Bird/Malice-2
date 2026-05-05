package net.maven.malady.core.events.server;

import net.maven.malady.Config;
import net.maven.malady.Malady;
import net.maven.malady.core.items.CoreItems;
import net.maven.malady.core.items.soul.SoulCrystal;
import net.maven.malady.core.items.soul.UndeadSoulCrystal;
import net.maven.malady.currency.api.CurrencyAPI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Random;


@EventBusSubscriber(modid = Malady.MODID, bus = EventBusSubscriber.Bus.GAME)
public class OnMobDeath {


    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        LivingEntity killedEntity = event.getEntity();

        // Get the killer
        if (event.getSource().getEntity() instanceof LivingEntity killer) {

            ResourceLocation killedRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(killedEntity.getType());
            String killedId = killedRegistryName.toString();

            // Check if it's in the config list of undead soul crystal monsters
            if (Config.undead_soul_crystal_mobs.contains(killedId)) {

                // Store the drop chance in a variable
                int dropChance = Config.undead_soul_crystal_dropchance;
                Random random = new Random();

                // Check random chance
                if (random.nextInt(100) < dropChance) {
                    // Drop the soul crystal
                    dropSoulCrystal(killedEntity, "undead", 1);
                }
            }

            // Do not continue if the killer is not a player
            if (!(killer instanceof Player)) {
                return;
            }

            // Check if this mob dropped currency
            if (Config.currency_mobs.contains(killedId)) {
                Random random = new Random();

                if (random.nextInt(100) < 10) {
                    Player player = (Player) killer;
                    // Give the currency
                    CurrencyAPI.addCurrency(player, 1);
                }
            }

        }


    }

    private static void dropSoulCrystal(LivingEntity killedEntity, String crystal, int amount) {

            if ( crystal.equals("undead") ) {
                ItemStack itemStack = new ItemStack(CoreItems.UNDEAD_SOUL_CRYSTAL.get());
                itemStack.setCount(amount);
                killedEntity.spawnAtLocation(itemStack);
            }

    }

}



