package net.buycraft.plugin.bedrock.shared.bedrock.tasks;

import net.buycraft.plugin.bedrock.IBuycraftPlatform;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;
import net.buycraft.plugin.bedrock.execution.PlayerCommandExecutor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerJoinCheckTask implements Runnable {
    private final IBuycraftPlatform platform;
    private final Queue<QueuedPlayer> queuedPlayers = new ConcurrentLinkedQueue<>();

    public PlayerJoinCheckTask(IBuycraftPlatform platform) {
        this.platform = platform;
    }

    @Override
    public void run() {
        QueuedPlayer qp = queuedPlayers.poll();
        if (qp != null) {
            platform.executeAsync(new PlayerCommandExecutor(qp, platform));
        }
    }

    public void queue(QueuedPlayer player) {
        queuedPlayers.add(player);
    }
}
