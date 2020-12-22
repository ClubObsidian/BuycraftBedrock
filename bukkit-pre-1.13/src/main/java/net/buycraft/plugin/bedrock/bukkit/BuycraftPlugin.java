package net.buycraft.plugin.bedrock.bukkit;

public class BuycraftPlugin extends BuycraftPluginBase {
    @Override
    protected BukkitBuycraftPlatformBase createBukkitPlatform() {
        return new BukkitBuycraftPlatform(this);
    }
}
