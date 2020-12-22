package net.buycraft.plugin.bedrock;

import com.google.common.base.Preconditions;

import java.util.UUID;

public final class UuidUtil {
    private UuidUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Translates a Mojang-style UUID into an UUID Java can use. The Buycraft plugin API returns all results with
     * Mojang-style UUIDs.
     *
     * @param id the Mojang UUID to use
     * @return the Java UUID
     */
    public static UUID xuidToJavaUuid(String id) {
        Preconditions.checkNotNull(id, "id");
        Preconditions.checkArgument(id.matches("[0-9]+"), "Not a valid xuid.");
        long xuid = Long.parseLong(id);
        return createJavaPlayerId(xuid);
    }

    private static UUID createJavaPlayerId(long xuid) {
        return new UUID(0, xuid);
    }
}
