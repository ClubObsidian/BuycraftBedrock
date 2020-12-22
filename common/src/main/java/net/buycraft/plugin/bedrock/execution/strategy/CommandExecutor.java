package net.buycraft.plugin.bedrock.execution.strategy;

public interface CommandExecutor {
    void queue(ToRunQueuedCommand command);
}
