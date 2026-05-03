package kawaii.addon.v2.real.util;

import net.minecraft.client.Minecraft;

public class PlayerPosition {

    private final Minecraft mc;

    public PlayerPosition(Minecraft mc) {
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
        return mc.level.dimension().identifier().getPath();
    }
}
