package kawaii.addon.v2.real.util;

import net.minecraft.client.MinecraftClient;

public class PlayerPosition {

    private final MinecraftClient mc;

    public PlayerPosition(MinecraftClient mc) {
        this.mc = mc;
    }

    private int floor(double value) {
        return (int) Math.floor(value);
    }

    public int getX() {
        return floor(mc.player.getX());
    }

    public int getY() {
        return floor(mc.player.getY());
    }

    public int getZ() {
        return floor(mc.player.getZ());
    }

    public String getDimension() {
        return mc.world.getRegistryKey().getValue().getPath();
    }
}
