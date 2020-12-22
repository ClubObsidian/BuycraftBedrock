package net.buycraft.plugin.bedrock.bukkit.tasks;

import net.buycraft.plugin.bedrock.bukkit.BuycraftPluginBase;
import net.buycraft.plugin.bedrock.data.responses.CheckoutUrlResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SendCheckoutLink implements Runnable {
    private BuycraftPluginBase plugin;
    private int id;
    private Player player;
    private Boolean isCategory;
    private CommandSender sender;

    public SendCheckoutLink(BuycraftPluginBase plugin, int id, Player p) {
        this.plugin = plugin;
        this.id = id;
        this.player = p;
        this.isCategory = false;
        this.sender = null;
    }

    public SendCheckoutLink(BuycraftPluginBase plugin, int id, Player p, boolean isCategory, CommandSender sender) {
        this.plugin = plugin;
        this.id = id;
        this.player = p;
        this.isCategory = isCategory;
        this.sender = sender;
    }

    @Override
    public void run() {
        CheckoutUrlResponse response;
        try {
            if (!isCategory) {
                response = plugin.getApiClient().getCheckoutUri(player.getName(), id).execute().body();
            } else {
                response = plugin.getApiClient().getCategoryUri(player.getName(), id).execute().body();
            }
        } catch (IOException e) {
            if (sender == null)
                player.sendMessage(ChatColor.RED + plugin.getI18n().get("cant_check_out") + " " + e.getMessage());
            else
                sender.sendMessage(ChatColor.RED + plugin.getI18n().get("cant_check_out") + " " + e.getMessage());
            return;
        }
        if (!isCategory) {
            player.sendMessage(ChatColor.STRIKETHROUGH + "                                            ");
            player.sendMessage(ChatColor.GREEN + plugin.getI18n().get("to_buy_this_package"));
            player.sendMessage(ChatColor.BLUE + ChatColor.UNDERLINE.toString() + response.getUrl());
            player.sendMessage(ChatColor.STRIKETHROUGH + "                                            ");
        } else {
            player.sendMessage(ChatColor.STRIKETHROUGH + "                                            ");
            player.sendMessage(ChatColor.GREEN + plugin.getI18n().get("to_view_this_category"));
            player.sendMessage(ChatColor.BLUE + ChatColor.UNDERLINE.toString() + response.getUrl());
            player.sendMessage(ChatColor.STRIKETHROUGH + "                                            ");
        }
    }
}
