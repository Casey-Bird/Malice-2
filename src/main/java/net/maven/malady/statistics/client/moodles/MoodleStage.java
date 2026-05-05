package net.maven.malady.statistics.client.moodles;

public enum MoodleStage {
    GREAT("great"),  // Suffix for great texture
    BAD("bad"),      // Suffix for bad texture
    TERRIBLE("terrible"); // Suffix for terrible texture

    private final String textureSuffix;

    MoodleStage(String textureSuffix) {
        this.textureSuffix = textureSuffix;
    }

    public String getTextureSuffix() {
        return textureSuffix;
    }
}
