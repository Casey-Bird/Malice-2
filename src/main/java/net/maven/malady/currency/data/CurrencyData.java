package net.maven.malady.currency.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class CurrencyData implements INBTSerializable<CompoundTag> {

    private int currency = 0;

    public CurrencyData(IAttachmentHolder holder) {
        // Constructor with holder, can be used if needed
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int amount) {
        this.currency = Math.max(0, amount);
    }

    public void addCurrency(int amount) {
        this.currency = Math.max(0, this.currency + amount);
    }

    public void removeCurrency(int amount) {
        this.currency = Math.max(0, this.currency - amount);
    }

    public boolean hasEnough(int amount) {
        return this.currency >= amount;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("currency", currency);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.currency = tag.getInt("currency");
    }

    // Static helper to get currency data from player
    public static CurrencyData get(Player player) {
        return player.getData(CurrencyAttachments.CURRENCY_DATA);
    }



}
