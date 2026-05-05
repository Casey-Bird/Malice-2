package net.maven.malady.currency.items;

import net.maven.malady.Malady;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CurrencyItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Malady.MODID);

    public static final DeferredItem<Item> CURRENCY_ITEM = ITEMS.register(
            "currency_item",
            () -> new Item(new Item.Properties())
    );


}
