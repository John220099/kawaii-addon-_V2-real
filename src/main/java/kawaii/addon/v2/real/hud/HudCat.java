package kawaii.addon.v2.real.hud;

import kawaii.addon.v2.real.AddonTemplate;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;

public class HudCat extends HudElement {
    public static final HudElementInfo<HudCat> INFO = new HudElementInfo<>(AddonTemplate.HUD_GROUP, "cat-hud", "Displays a cat icon.", HudCat::new);
    public static final Identifier TEXTURE = Identifier.of("kawaii-addon", "/hud/cat1.png");
    public HudCat() {
        super(INFO);
    }

    private final SettingGroup sg = settings.getDefaultGroup();

    private final Setting<Integer> size = sg.add(new IntSetting.Builder()
        .name("size")
        .description("How many cats to show.")
        .defaultValue(5)
        .min(1)
        .sliderMin(1)
        .sliderMax(20)
        .build()
    );

    @Override
    public void render(HudRenderer renderer) {
        int n = size.get();
        setSize(64 * n, 64 * n);
        renderer.texture(TEXTURE, x, y, getWidth(), getHeight(), Color.WHITE);
    }
}
