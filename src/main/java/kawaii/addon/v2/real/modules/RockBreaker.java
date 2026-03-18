package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import kawaii.addon.v2.real.util.PlayerPosition;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

public class RockBreaker extends Module {

    public RockBreaker() {
        super(KawaiiAddon.CATEGORY, "RockBreaker", "might break bedrock on some servers.");
    }

    MinecraftClient client = MinecraftClient.getInstance();

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player != null) {
            PlayerPosition pos = new PlayerPosition(mc);
            int y = pos.getY();

            if (mc.world.getRegistryKey() == World.NETHER & y == 5) {
                assert client.player != null;
                String currentPrefix = Config.get().prefix.get();
                String message = String.format(currentPrefix + "vclip -15");
                client.player.networkHandler.sendChatMessage(message);
            }
        }
    }
}
