package net.buycraft.plugin.bedrock.bukkit.command;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    void execute(CommandSender sender, String[] args);

    String getDescription();
}