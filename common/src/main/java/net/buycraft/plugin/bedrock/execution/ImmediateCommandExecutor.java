package net.buycraft.plugin.bedrock.execution;

import net.buycraft.plugin.bedrock.IBuycraftPlatform;
import net.buycraft.plugin.bedrock.data.QueuedCommand;
import net.buycraft.plugin.bedrock.data.responses.QueueInformation;
import net.buycraft.plugin.bedrock.execution.strategy.ToRunQueuedCommand;

import java.io.IOException;
import java.util.logging.Level;

public class ImmediateCommandExecutor implements Runnable {
    private final IBuycraftPlatform platform;

    public ImmediateCommandExecutor(final IBuycraftPlatform platform) {
        this.platform = platform;
    }

    @Override
    public void run() {
        if (platform.getApiClient() == null) {
            return; // no API client
        }

        QueueInformation information;
        try {
            // Retrieve offline command queue.
            information = platform.getApiClient().retrieveOfflineQueue().execute().body();
        } catch (IOException e) {
            platform.log(Level.SEVERE, "Could not fetch command queue", e);
            return;
        }

        // Queue commands for later.
        for (QueuedCommand command : information.getCommands()) {
            platform.getExecutor().queue(new ToRunQueuedCommand(command.getPlayer(), command, false));
        }
    }
}
