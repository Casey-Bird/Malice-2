package net.maven.malady.statistics.data;

import net.maven.malady.statistics.data.MoodData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientMoodCache {

    private static final Map<UUID, MoodData> CACHE = new ConcurrentHashMap<>();

    public static void update(UUID playerId, MoodData data) {
        CACHE.put(playerId, data);
    }

    public static MoodData get(UUID playerId) {
        return CACHE.getOrDefault(playerId, new MoodData(0));
    }

    public static void remove(UUID playerId) {
        CACHE.remove(playerId);
    }

    public static void clear() {
        CACHE.clear();
    }
}