package net.buycraft.plugin.bedrock.bukkit.command;

import net.buycraft.plugin.bedrock.bukkit.BuycraftPluginBase;
import net.buycraft.plugin.bedrock.bukkit.tasks.SendCheckoutLink;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendLinkSubcommand implements Subcommand {
    private final BuycraftPluginBase plugin;

    public SendLinkSubcommand(final BuycraftPluginBase plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("buycraft.admin")) {
            sender.sendMessage(ChatColor.RED + plugin.getI18n().get("no_permission"));
            return;
        }

        if (args.length != 3 || !(args[1].equalsIgnoreCase("package") || args[1].equalsIgnoreCase("category")) || !StringUtils.isNumeric(args[2])) {
            sender.sendMessage(ChatColor.RED + "Incorrect syntax: /tebex sendlink <player> package|category <id>");
            return;
        }

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null || !p.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is not online!");
            return;
        }

        if (args[1].equalsIgnoreCase("package")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new SendCheckoutLink(plugin, Integer.valueOf(args[2]), p, false, sender));
        } else if (args[1].equalsIgnoreCase("category")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new SendCheckoutLink(plugin, Integer.valueOf(args[2]), p, true, sender));
        }
    }

    @Override
    public String getDescription() {
        return plugin.getI18n().get("usage_sendlink");
    }
}
