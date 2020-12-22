package net.buycraft.plugin.bedrock.platform;

import java.util.Locale;

public enum PlatformType {
    BUKKIT,
    BUNGEECORD,
    SPONGE,
    NUKKIT,
    FORGE,
    VELOCITY,
    NONE;

    public String platformName() {
        return name().toLowerCase(Locale.US);
    }
}
