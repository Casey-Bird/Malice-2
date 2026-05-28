package net.maven.malady.statistics.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MoodData(int moodAmount) {

    public static final Codec<MoodData> CODEC = Codec.INT.xmap(MoodData::new, MoodData::moodAmount);
    public static final StreamCodec<ByteBuf, MoodData> STREAM_CODEC =
            ByteBufCodecs.INT.map(MoodData::new, MoodData::moodAmount);

    public MoodData increment(int amount) {
        return new MoodData(moodAmount + amount);
    }

    public MoodData decrement(int amount) {

        return new MoodData(Math.max(0, moodAmount - amount));
    }

    public MoodData set(int value) {
        return new MoodData(value);
    }


}
