package kawaii.addon.v2.real.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class Cuddle extends Command {
    public Cuddle() {
        super("cuddler", "Sends ur coords in public chat :D");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null) {
                //if ur seeing this is for CoOrdLeakerCommand this doesn't execute on its own!
                assert mc.player != null;
                int x = (int) Math.floor(mc.player.getX());
                int y = (int) Math.floor(mc.player.getY());
                int z = (int) Math.floor(mc.player.getZ());
                String message = String.format("Cuddle with me at coords owo: X: %d, Y: %d, Z: %d", x, y, z);
                client.player.networkHandler.sendChatMessage(message);
            } else {
                error("skill issue thb.");
            }

            return SINGLE_SUCCESS;
        });
    }
}
