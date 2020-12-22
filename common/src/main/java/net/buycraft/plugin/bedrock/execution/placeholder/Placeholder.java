package net.buycraft.plugin.bedrock.execution.placeholder;

import net.buycraft.plugin.bedrock.data.QueuedCommand;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;

public interface Placeholder {
    String replace(String command, QueuedPlayer player, QueuedCommand queuedCommand);
}
