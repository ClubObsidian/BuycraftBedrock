package net.buycraft.plugin.bedrock.bukkit.util;

import net.buycraft.plugin.bedrock.shared.bedrock.config.signs.storage.SerializedBlockLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class BukkitSerializedBlockLocation {
    private BukkitSerializedBlockLocation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static SerializedBlockLocation create(Location location) {
        return new SerializedBlockLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Location toBukkit(SerializedBlockLocation location) {
        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }
}
