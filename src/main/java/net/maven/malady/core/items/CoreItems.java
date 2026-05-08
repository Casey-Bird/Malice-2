package net.maven.malady.core.items;

import net.maven.malady.Malady;
import net.maven.malady.core.items.soul.BrimstoneSoulCrystal;
import net.maven.malady.core.items.soul.InfernalSoulCrystal;
import net.maven.malady.core.items.soul.LesserSoulCrystal;
import net.maven.malady.core.items.soul.UndeadSoulCrystal;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CoreItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Malady.MODID);


    public static final Supplier<Item> LESSER_SOUL_CRYSTAL = ITEMS.register(
            "lesser_soul_crystal",
            LesserSoulCrystal::new
    );

    public static final Supplier<Item> UNDEAD_SOUL_CRYSTAL = ITEMS.register(
            "undead_soul_crystal",
            UndeadSoulCrystal::new
    );


    public static final Supplier<Item> BRIMSTONE_SOUL_CRYSTAL = ITEMS.register(
            "brimstone_soul_crystal",
            BrimstoneSoulCrystal::new
    );

    public static final Supplier<Item> INFERNAL_SOUL_CRYSTAL = ITEMS.register(
            "infernal_soul_crystal",
            InfernalSoulCrystal::new
    );

}
