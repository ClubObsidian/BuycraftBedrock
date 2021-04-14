package net.buycraft.plugin.bedrock.bukkit.util;

import java.util.UUID;

public final class BedrockUtil {

    public static boolean isBedrockUUID(UUID uuid) {
        return uuid.getMostSignificantBits() == 0;
    }

    public static long getXUID(UUID uuid) {
        return uuid.getLeastSignificantBits();
    }

    public static String getXUIDStr(UUID uuid) {
        return String.valueOf(getXUID(uuid));
    }

    private BedrockUtil() {}
}
