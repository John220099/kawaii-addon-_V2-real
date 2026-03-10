package kawaii.addon.v2.real.modules;

import kawaii.addon.v2.real.KawaiiAddon;
import kawaii.addon.v2.real.util.SwapUtil;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import java.util.ArrayList;
import java.util.List;

public class AntiWeb extends Module {

    public enum SwapMode {
        Normal,
        Silent,
        None
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SwapMode> swapMode = sgGeneral.add(new EnumSetting.Builder<SwapMode>()
        .name("swap-mode")
        .description("300 iq swap code.")
        .defaultValue(SwapMode.Normal)
        .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("are we there yet?")
        .defaultValue(0.5)
        .min(0.1)
        .sliderMax(2.0)
        .build()
    );

    private final Setting<Boolean> notify = sgGeneral.add(new BoolSetting.Builder()
        .name("notify")
        .description("snitches.")
        .defaultValue(false)
        .build()
    );

    private final List<BlockPos> mined = new ArrayList<>();
    private boolean swapped = false;

    public AntiWeb() {
        super(KawaiiAddon.CATEGORY, "AntiWeb", "funny packet mine module because NoSlow not work for some reason on servers that use grim ac fucking retards.");
    }

    @Override
    public void onDeactivate() {
        mined.clear();
        restoreIfSwapped();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null || mc.getNetworkHandler() == null) return;

        double r = range.get();
        Box bodyBox = mc.player.getBoundingBox().expand(r);
        Box headBox = new Box(
            mc.player.getX() - r, mc.player.getEyeY() - r, mc.player.getZ() - r,
            mc.player.getX() + r, mc.player.getEyeY() + r, mc.player.getZ() + r
        );

        List<BlockPos> webs = new ArrayList<>();
        BlockPos.stream(bodyBox.union(headBox))
            .filter(pos -> mc.world.getBlockState(pos).getBlock() == Blocks.COBWEB)
            .forEach(pos -> webs.add(pos.toImmutable()));

        mined.removeIf(pos -> !webs.contains(pos));

        if (webs.isEmpty()) {
            restoreIfSwapped();
            return;
        }

        int swordSlot = SwapUtil.findInHotbar(ItemTags.SWORDS);
        boolean canSwap = swapMode.get() != SwapMode.None && swordSlot != -1;

        if (canSwap && !swapped) {
            if (swapMode.get() == SwapMode.Silent) {
                SwapUtil.swapSilent(swordSlot);
            } else {
                SwapUtil.swapNormal(swordSlot);
            }
            swapped = true;
        }

        int broke = 0;

        for (BlockPos pos : webs) {
            if (mined.contains(pos)) continue;
            Direction face = getClosestFace(pos);

            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, face));
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, face));

            mined.add(pos);
            broke++;
        }

        if (broke > 0) {
            restoreIfSwapped();
            if (notify.get()) info("Broke " + broke + " cobweb(s).");
        }
    }

    private void restoreIfSwapped() {
        if (!swapped) return;
        if (swapMode.get() == SwapMode.Silent) {
            SwapUtil.swapBack();
        } else {
            SwapUtil.swapBackNormal();
        }
        swapped = false;
    }

    private Direction getClosestFace(BlockPos pos) {
        double dx = mc.player.getX()    - (pos.getX() + 0.5);
        double dy = mc.player.getEyeY() - (pos.getY() + 0.5);
        double dz = mc.player.getZ()    - (pos.getZ() + 0.5);
        double ax = Math.abs(dx), ay = Math.abs(dy), az = Math.abs(dz);
        if (ax > ay && ax > az) return dx > 0 ? Direction.EAST  : Direction.WEST;
        if (ay > ax && ay > az) return dy > 0 ? Direction.UP    : Direction.DOWN;
        return dz > 0 ? Direction.SOUTH : Direction.NORTH;
    }
}
