package net.buycraft.plugin.bedrock.bukkit.signs.purchases;

import net.buycraft.plugin.bedrock.bukkit.BuycraftPluginBase;
import net.buycraft.plugin.bedrock.bukkit.tasks.RecentPurchaseSignUpdateApplication;
import net.buycraft.plugin.bedrock.bukkit.util.BukkitSerializedBlockLocation;
import net.buycraft.plugin.bedrock.shared.bedrock.config.signs.storage.RecentPurchaseSignPosition;
import net.buycraft.plugin.bedrock.shared.bedrock.config.signs.storage.SerializedBlockLocation;
import net.buycraft.plugin.bedrock.bukkit.tasks.RecentPurchaseSignUpdateFetcher;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;

public class RecentPurchaseSignListener implements Listener {
    private final BuycraftPluginBase plugin;

    public RecentPurchaseSignListener(final BuycraftPluginBase plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        boolean ourSign;
        try {
            ourSign = Arrays.asList("[buycraft_rp]", "[tebex_rp]").contains(event.getLine(0).toLowerCase());
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        if (!ourSign) return;
        if (!event.getPlayer().hasPermission("buycraft.admin")) {
            event.getPlayer().sendMessage(ChatColor.RED + "You can't create Buycraft signs.");
            return;
        }
        int pos;
        try {
            pos = Integer.parseInt(StringUtils.trimToEmpty(event.getLine(1)));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            event.getPlayer().sendMessage(ChatColor.RED + "The second line must be a number.");
            return;
        }

        if (pos <= 0) {
            event.getPlayer().sendMessage(ChatColor.RED + "The second line can not be negative or zero.");
            return;
        }

        if (pos > 100) {
            event.getPlayer().sendMessage(ChatColor.RED + "No more than the 100 most recent purchases can be displayed on signs.");
            return;
        }

        plugin.getRecentPurchaseSignStorage().addSign(new RecentPurchaseSignPosition(BukkitSerializedBlockLocation.create(
                event.getBlock().getLocation()), pos));
        event.getPlayer().sendMessage(ChatColor.GREEN + "Added new recent purchase sign!");

        for (int i = 0; i < 4; i++) {
            event.setLine(i, "");
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new RecentPurchaseSignUpdateFetcher(plugin));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getPlatform().getSignMaterials().contains(event.getBlock().getType())) {
            SerializedBlockLocation location = BukkitSerializedBlockLocation.create(event.getBlock().getLocation());
            if (plugin.getRecentPurchaseSignStorage().containsLocation(location)) {
                if (!event.getPlayer().hasPermission("buycraft.admin")) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to break this sign.");
                    event.setCancelled(true);
                    return;
                }
                if (plugin.getRecentPurchaseSignStorage().removeSign(location)) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Removed recent purchase sign!");
                }
            }
            return;
        }

        for (BlockFace face : RecentPurchaseSignUpdateApplication.FACES) {
            Location onFace = event.getBlock().getRelative(face).getLocation();
            SerializedBlockLocation onFaceSbl = BukkitSerializedBlockLocation.create(onFace);
            if (plugin.getRecentPurchaseSignStorage().containsLocation(onFaceSbl)) {
                if (!event.getPlayer().hasPermission("buycraft.admin")) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to break this sign.");
                    event.setCancelled(true);
                    return;
                }
                if (plugin.getRecentPurchaseSignStorage().removeSign(onFaceSbl)) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Removed recent purchase sign!");
                }
            }
        }
    }
}
