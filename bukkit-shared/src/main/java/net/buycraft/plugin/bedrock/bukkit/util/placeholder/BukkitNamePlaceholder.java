package net.buycraft.plugin.bedrock.bukkit.util.placeholder;

import net.buycraft.plugin.bedrock.UuidUtil;
import net.buycraft.plugin.bedrock.data.QueuedCommand;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;
import net.buycraft.plugin.bedrock.execution.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.regex.Pattern;

public class BukkitNamePlaceholder implements Placeholder {
    private static final Pattern REPLACE_NAME = Pattern.compile("[{\\(<\\[](name|player|username)[}\\)>\\]]", Pattern.CASE_INSENSITIVE);

    @Override
    public String replace(String command, QueuedPlayer player, QueuedCommand queuedCommand) {
        if (player.getUuid() == null || player.getUuid().equals("")) {
            return REPLACE_NAME.matcher(command).replaceAll(player.getName());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UuidUtil.xuidToJavaUuid(player.getUuid()));
        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            return REPLACE_NAME.matcher(command).replaceAll(player.getName());
        }

        return REPLACE_NAME.matcher(command).replaceAll(offlinePlayer.getName());
    }
}
