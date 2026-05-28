package net.maven.malady.statistics.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;


/// Keeps track of a player's state of Tiredness (how sleepy they may be)
public record TiredData(int tiredAmount) {
    public static final Codec<TiredData> CODEC = Codec.INT.xmap(TiredData::new, TiredData::tiredAmount);
    public static final StreamCodec<ByteBuf, TiredData> STREAM_CODEC =
            ByteBufCodecs.INT.map(TiredData::new, TiredData::tiredAmount);

    public TiredData increment() {
        return new TiredData(tiredAmount + 1);
    }

    public TiredData decrement() {
        return new TiredData(Math.max(0, tiredAmount - 1));
    }

    public TiredData set(int value) {
        return new TiredData(value);
    }
}