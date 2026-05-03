package kawaii.addon.v2.real.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

public class Dupe extends Command {
    public Dupe() {
        super("dupe", "Client-side fake dupe command");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(_ -> {
            info("Get baited Dumb Ass!");
            return SINGLE_SUCCESS;
        });
    }
}
