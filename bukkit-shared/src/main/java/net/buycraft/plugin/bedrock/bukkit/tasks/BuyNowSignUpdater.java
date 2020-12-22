package net.buycraft.plugin.bedrock.bukkit.tasks;

import net.buycraft.plugin.bedrock.bukkit.BuycraftPluginBase;
import net.buycraft.plugin.bedrock.bukkit.util.BukkitSerializedBlockLocation;
import net.buycraft.plugin.bedrock.data.Package;
import net.buycraft.plugin.bedrock.shared.bedrock.config.signs.storage.SavedBuyNowSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.Currency;
import java.util.List;

public class BuyNowSignUpdater implements Runnable {
    private final BuycraftPluginBase plugin;

    public BuyNowSignUpdater(final BuycraftPluginBase plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (SavedBuyNowSign sign : plugin.getBuyNowSignStorage().getSigns()) {
            Package p = plugin.getListingUpdateTask().getPackageById(sign.getPackageId());
            if (p == null) {
                plugin.getLogger().warning(String.format("Sign at %d, %d, %d in world %s does not have a valid package assigned to it.", sign.getLocation().getX(), sign.getLocation().getY(), sign.getLocation().getZ(), sign.getLocation().getWorld()));
                continue;
            }

            Location location = BukkitSerializedBlockLocation.toBukkit(sign.getLocation());
            if (location.getWorld() == null) {
                plugin.getLogger().warning(String.format("Sign at %d, %d, %d exists in non-existent world %s!",
                        sign.getLocation().getX(), sign.getLocation().getY(), sign.getLocation().getZ(), sign.getLocation().getWorld()));
                continue;
            }

            Block b = location.getBlock();
            if (!(plugin.getPlatform().getSignMaterials().contains(b.getType()))) {
                plugin.getLogger().warning(String.format("Sign at %d, %d, %d in world %s is not a sign in the world!",
                        sign.getLocation().getX(), sign.getLocation().getY(), sign.getLocation().getZ(), sign.getLocation().getWorld()));
                continue;
            }

            Currency currency = Currency.getInstance(plugin.getServerInformation().getAccount().getCurrency().getIso4217());
            Sign worldSign = (Sign) b.getState();
            List<String> lines = plugin.getBuyNowSignLayout().format(currency, p);
            for (int i = 0; i < 4; i++) {
                worldSign.setLine(i, ChatColor.translateAlternateColorCodes('&', i >= lines.size() ? "" : lines.get(i)));
            }
            worldSign.update();
        }
    }
}
