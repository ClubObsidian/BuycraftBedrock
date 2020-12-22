package net.buycraft.plugin.bedrock.execution.placeholder;

import net.buycraft.plugin.bedrock.data.QueuedCommand;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;

import java.util.regex.Pattern;

public class NamePlaceholder implements Placeholder {
    private static final Pattern REPLACE_NAME = Pattern.compile("[{\\(<\\[](name|player|username)[}\\)>\\]]", Pattern.CASE_INSENSITIVE);

    @Override
    public String replace(String command, QueuedPlayer player, QueuedCommand queuedCommand) {
        return REPLACE_NAME.matcher(command).replaceAll(player.getName());
    }
}
