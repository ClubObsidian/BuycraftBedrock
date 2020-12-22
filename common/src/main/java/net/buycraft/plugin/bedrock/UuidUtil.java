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
    public static UUID mojangUuidToJavaUuid(String id) {
        Preconditions.checkNotNull(id, "id");
        Preconditions.checkArgument(id.matches("^[a-z0-9]{32}"), "Not a valid Mojang UUID.");

        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" +
                id.substring(16, 20) + "-" + id.substring(20, 32));
    }
}
