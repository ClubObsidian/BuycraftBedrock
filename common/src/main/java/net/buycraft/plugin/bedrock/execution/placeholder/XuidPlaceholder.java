package net.buycraft.plugin.bedrock.execution.placeholder;

import net.buycraft.plugin.bedrock.data.QueuedCommand;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;

import java.util.regex.Pattern;

public class XuidPlaceholder implements Placeholder {
    private static final Pattern REPLACE_UUID = Pattern.compile("[{(<\\[]id[})>\\]]", Pattern.CASE_INSENSITIVE);

    @Override
    public String replace(String command, QueuedPlayer player, QueuedCommand queuedCommand) {
        if (player.getUuid() == null) {
            return command; // can't replace UUID for offline mode
        }
        return REPLACE_UUID.matcher(command).replaceAll(player.getUuid());
    }
}
