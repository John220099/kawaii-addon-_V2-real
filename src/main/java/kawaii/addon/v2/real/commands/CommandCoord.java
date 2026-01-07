package kawaii.addon.v2.real.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class CommandCoord extends Command {
    public CommandCoord() {
        super("coordleaker", "Sends ur coords in public chat :D");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null) {
                int x = client.player.getBlockX();
                int y = client.player.getBlockY();
                int z = client.player.getBlockZ();
                String message = String.format("Hi nerds my coords are: X: %d, Y: %d, Z: %d", x, y, z);
                client.player.networkHandler.sendChatMessage(message);
            } else {
                error("lol.");
            }

            return SINGLE_SUCCESS;
        });
    }
}
