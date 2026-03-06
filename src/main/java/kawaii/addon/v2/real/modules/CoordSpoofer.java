package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.settings.*;

import net.minecraft.util.math.random.Random;

public class CoordSpoofer extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Integer> seed = sgGeneral.add(new IntSetting.Builder()
        .name("seed")
        .description("Seed used to offset coordinates.")
        .defaultValue(Random.create().nextBetween(-1000000, 1000000))
        .min(-1000000)
        .max(1000000)
        .build()
    );

    public CoordSpoofer() {
        super(KawaiiAddon.CATEGORY, "coord-spoofer", "Spoofs coordinates in the debug screen.");
    }
}
