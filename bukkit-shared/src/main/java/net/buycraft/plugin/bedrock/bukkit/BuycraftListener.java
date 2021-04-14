package net.buycraft.plugin.bedrock.bukkit;

import net.buycraft.plugin.bedrock.bukkit.util.BedrockUtil;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;
import net.buycraft.plugin.bedrock.data.ServerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.UUID;

public class BuycraftListener implements Listener {
    private final BuycraftPluginBase plugin;

    public BuycraftListener(final BuycraftPluginBase plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getApiClient() == null) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!BedrockUtil.isBedrockUUID(uuid)) {
            return;
        }

        String xuid = BedrockUtil.getXUIDStr(uuid);

        plugin.getServerEventSenderTask().queueEvent(new ServerEvent(
                xuid,
                event.getPlayer().getName(),
                event.getPlayer().getAddress().getAddress().getHostAddress(),
                ServerEvent.JOIN_EVENT,
                new Date()
        ));

        QueuedPlayer qp = plugin.getDuePlayerFetcher().fetchAndRemoveDuePlayer(event.getPlayer().getName());
        if (qp != null) {
            plugin.getPlayerJoinCheckTask().queue(qp);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!plugin.getConfiguration().isDisableBuyCommand()) {
            for (String s : plugin.getConfiguration().getBuyCommandName()) {
                if (event.getMessage().substring(1).equalsIgnoreCase(s) ||
                        event.getMessage().regionMatches(true, 1, s + " ", 0, s.length() + 1)) {
                    event.setCancelled(true);
                    plugin.getViewCategoriesGUI().open(event.getPlayer());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getApiClient() == null) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!BedrockUtil.isBedrockUUID(uuid)) {
            return;
        }
        String xuid = BedrockUtil.getXUIDStr(uuid);

        plugin.getServerEventSenderTask().queueEvent(new ServerEvent(
                xuid,
                event.getPlayer().getName(),
                event.getPlayer().getAddress().getAddress().getHostAddress(),
                ServerEvent.LEAVE_EVENT,
                new Date()
        ));
    }
}