package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import kawaii.addon.v2.real.util.SwapUtil;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.registry.tag.ItemTags;

public class SwordSwap extends Module {

    public enum SwapMode {
        Normal,
        Silent
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SwapMode> mode = sgGeneral.add(new EnumSetting.Builder<SwapMode>()
        .name("mode")
        .description("swap mode.")
        .defaultValue(SwapMode.Silent)
        .build()
    );

    private boolean swapped = false;

    public SwordSwap() {
        super(KawaiiAddon.CATEGORY, "SwordSwap", "used for testing Silent hotbar switching.");
    }

    @Override
    public void onDeactivate() {
        restore();
        swapped = false;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        int swordSlot = SwapUtil.findInHotbar(ItemTags.SWORDS);

        if (swordSlot == -1) {
            if (swapped) {
                restore();
                swapped = false;
            }
            return;
        }

        if (!swapped) {
            if (mode.get() == SwapMode.Silent) {
                SwapUtil.swapSilent(swordSlot);
            } else {
                SwapUtil.swapNormal(swordSlot);
            }
            swapped = true;
        }
    }

    private void restore() {
        if (mode.get() == SwapMode.Silent) {
            SwapUtil.swapBack();
        } else {
            SwapUtil.swapBackNormal();
        }
    }
}
