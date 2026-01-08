package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.render.Render3DEvent;



public class HeadPatTurn extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();

    private final Setting<Integer> speed = sg.add(new IntSetting.Builder()
        .name("speed")
        .description("spin speed!!!")
        .defaultValue(20)
        .min(20)
        .sliderMin(20)
        .sliderMax(100)
        .build()
    );

    public HeadPatTurn() {
        super(KawaiiAddon.CATEGORY, "HeadPatTurn", "Spins you right round (flo rida).");
    }
    //So fucking buggy but that's fun?! so we are keeping it :)
    @EventHandler
    private void onRender(Render3DEvent event) {
        if (mc.player == null) return;

        float spin = speed.get();

        // Rotate ONLY the model
        mc.player.headYaw += spin;
        mc.player.bodyYaw += spin;

        // Prevent snapping
        mc.player.lastHeadYaw = mc.player.headYaw;
        mc.player.lastBodyYaw = mc.player.bodyYaw;
    }
}

