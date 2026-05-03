package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.RandomSource;
import meteordevelopment.meteorclient.settings.*;

public class CoordSpoofer extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Integer> seed = sgGeneral.add(new IntSetting.Builder()
        .name("seed")
        .description("Seed used to offset coordinates.")
        .defaultValue(RandomSource.create().nextIntBetweenInclusive(-1000000, 1000000))
        .min(0)
        .sliderMax(1000000)
        .build()
    );

    public CoordSpoofer() {
        super(KawaiiAddon.CATEGORY, "coord-spoofer", "Spoofs coordinates in the debug screen.");
    }
}
