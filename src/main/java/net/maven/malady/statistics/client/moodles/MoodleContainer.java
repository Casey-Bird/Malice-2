package net.maven.malady.statistics.client.moodles;

import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoodleContainer {
    private final Map<MoodleType, MoodleAnimation> moodles = new LinkedHashMap<>();

    public MoodleContainer() {
        for (MoodleType type : MoodleType.values()) {
            moodles.put(type, new MoodleAnimation(type));
        }
    }

    public void update(Player player) {
        // Update all moodles
        moodles.values().forEach(moodle -> moodle.update(player));
    }

    public List<MoodleAnimation> getActiveMoodles() {
        return moodles.values().stream()
                .filter(MoodleAnimation::isActive)
                .sorted(Comparator.comparingInt(m -> m.getType().getPosition()))
                .collect(Collectors.toList());
    }
}
