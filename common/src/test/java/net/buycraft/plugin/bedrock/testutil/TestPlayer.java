package net.buycraft.plugin.bedrock.testutil;

public class TestPlayer {
    private final int freeSlots;

    public TestPlayer(int freeSlots) {
        this.freeSlots = freeSlots;
    }

    public int getFreeSlots() {
        return freeSlots;
    }
}
