package net.maven.malady.statistics.client.moodles;

import com.momosoftworks.coldsweat.api.util.Temperature;
import net.maven.malady.Malady;
import net.maven.malady.statistics.api.MoodAPI;
import net.maven.malady.statistics.data.ClientMoodCache;
import net.maven.malady.statistics.data.StatisticAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum MoodleType {
    HUNGRY(
            0,
            "hunger", // Base texture name
            player -> {
                int foodLevel = player.getFoodData().getFoodLevel();
                if (foodLevel <= 4) return MoodleStage.TERRIBLE;
                if (foodLevel <= 8) return MoodleStage.BAD;
                if (foodLevel >= 18) return MoodleStage.GREAT;
                return null;
            }
    ),
    TIRED(
            3,
            "tired",
            player -> {

                return null;
            }
    ),
    HAPPY(
            3,
            "happy",
            player -> {
                int happiness_level = ClientMoodCache.get(player.getUUID()).moodAmount();
                System.out.println(happiness_level);
                // 0 or lower means the debuff is active //
                if (happiness_level <= 100) return MoodleStage.TERRIBLE;
                if (happiness_level <= 500) return MoodleStage.BAD;
                if (happiness_level >= 800) return MoodleStage.GREAT;
                return null;
            }
    ),
    COLD(
            4,
            "cold",
            player -> {
                double temperature = Temperature.get(player, Temperature.Trait.CORE);
                // 0 is normal // -25 is bad // -55 is dangerous
                if (temperature <= -55) return MoodleStage.TERRIBLE;
                if (temperature <= -25) return MoodleStage.BAD;

                return null;
            }
    ),
    HOT(
            4,
            "hot",
            player -> {
        double temperature = Temperature.get(player, Temperature.Trait.CORE);
        // 0 is normal // 25 is bad // 55 is dangerous
        if (temperature >= 55) return MoodleStage.TERRIBLE;
        if (temperature >= 25) return MoodleStage.BAD;

        return null;
    });

    private final int position;
    private final String textureBase;
    private final Function<Player, MoodleStage> stageChecker;
    private final Map<MoodleStage, ResourceLocation> textureCache = new HashMap<>();

    MoodleType(int position, String textureBase, Function<Player, MoodleStage> stageChecker) {
        this.position = position;
        this.textureBase = textureBase;
        this.stageChecker = stageChecker;
    }

    public int getPosition() {
        return position;
    }

    public ResourceLocation getTextureForStage(MoodleStage stage) {
        // Cache textures to avoid creating new ResourceLocations each frame

        return textureCache.computeIfAbsent(stage, s ->
                ResourceLocation.fromNamespaceAndPath(
                        Malady.MODID,
                        "textures/gui/statistics/moodle/" + textureBase + "_" + s.getTextureSuffix() + ".png"
                )
        );
    }

    public MoodleStage checkStage(Player player) {
        return stageChecker.apply(player);
    }
}