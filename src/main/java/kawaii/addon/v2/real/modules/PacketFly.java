package kawaii.addon.v2.real.modules;

/*
 * Credits to Fizi for sharing.
 */

import kawaii.addon.v2.real.KawaiiAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.Vec3;

public class PacketFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private boolean sendingCustom = false;

    private final Setting<Double> glideSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("glide-speed")
        .description("Downward glide when not pressing jump/sneak.")
        .defaultValue(0.05)
        .min(0.0)
        .sliderRange(0.0, 0.3)
        .build());

    private final Setting<Double> horizontalSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("horizontal-speed")
        .description("Horizontal packet push speed.")
        .defaultValue(0.20)
        .min(0.0)
        .sliderRange(0.0, 0.6)
        .build());

    public PacketFly() {
        super(KawaiiAddon.CATEGORY, "Packet Fly", "Packet flight .");
    }

    @Override
    public void onDeactivate() {
        if (mc.player != null) mc.player.setDeltaMovement(0, 0, 0);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null) return;

        boolean jump = mc.options.keyJump.isDown();
        boolean sneak = mc.options.keyShift.isDown();

        double motionY = (jump ^ sneak) ? (jump ? 0.0622 : -0.0622) : -glideSpeed.get();

        double forward = (mc.options.keyUp.isDown() ? 1.0 : 0.0) - (mc.options.keyDown.isDown() ? 1.0 : 0.0);
        double strafe  = (mc.options.keyRight.isDown()   ? 1.0 : 0.0) - (mc.options.keyLeft.isDown()  ? 1.0 : 0.0);

        double motionX = 0.0;
        double motionZ = 0.0;

        if (forward != 0.0 || strafe != 0.0) {
            double norm = Math.hypot(forward, strafe);
            forward /= norm;
            strafe  /= norm;

            double yawRad = Math.toRadians(mc.player.getYRot() + 90.0f);
            double sin = Math.sin(yawRad);
            double cos = Math.cos(yawRad);

            motionX = (forward * cos + strafe * sin) * horizontalSpeed.get();
            motionZ = (forward * sin - strafe * cos) * horizontalSpeed.get();
        }

        Vec3 next = mc.player.getKnownMovement().add(motionX, motionY, motionZ);

        mc.player.setDeltaMovement(motionX, motionY, motionZ);
        mc.player.setPos(next.x, next.y, next.z);
        mc.player.fallDistance = 0.0f;

        if (mc.getConnection() != null) {
            sendingCustom = true;
            try {
                // main position + rotation
                mc.getConnection().send(new ServerboundMovePlayerPacket.PosRot(next.x, next.y, next.z, mc.player.getYRot(), mc.player.getXRot(), false, false));
                mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(next.x, next.y - 42069.0, next.z, true, false));
            } finally {
                sendingCustom = false;
            }
        }
    }

    @EventHandler
    private void onSend(PacketEvent.Send event) {
        if (!sendingCustom && event.packet instanceof ServerboundMovePlayerPacket) event.cancel(); // replace
    }

    @EventHandler
    private void onReceive(PacketEvent.Receive event) {
        if (event.packet instanceof ClientboundContainerClosePacket) event.cancel(); // SPacketCloseWindow cancel
    }
}
