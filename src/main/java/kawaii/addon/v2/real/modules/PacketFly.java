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
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.util.math.Vec3d;

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
        if (mc.player != null) mc.player.setVelocity(0, 0, 0);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        boolean jump = mc.options.jumpKey.isPressed();
        boolean sneak = mc.options.sneakKey.isPressed();

        double motionY = (jump ^ sneak) ? (jump ? 0.0622 : -0.0622) : -glideSpeed.get();

        double forward = (mc.options.forwardKey.isPressed() ? 1.0 : 0.0) - (mc.options.backKey.isPressed() ? 1.0 : 0.0);
        double strafe  = (mc.options.rightKey.isPressed()   ? 1.0 : 0.0) - (mc.options.leftKey.isPressed()  ? 1.0 : 0.0);

        double motionX = 0.0;
        double motionZ = 0.0;

        if (forward != 0.0 || strafe != 0.0) {
            double norm = Math.hypot(forward, strafe);
            forward /= norm;
            strafe  /= norm;

            double yawRad = Math.toRadians(mc.player.getYaw() + 90.0f);
            double sin = Math.sin(yawRad);
            double cos = Math.cos(yawRad);

            motionX = (forward * cos + strafe * sin) * horizontalSpeed.get();
            motionZ = (forward * sin - strafe * cos) * horizontalSpeed.get();
        }

        Vec3d next = mc.player.getMovement().add(motionX, motionY, motionZ);

        mc.player.setVelocity(motionX, motionY, motionZ);
        mc.player.setPosition(next.x, next.y, next.z);
        mc.player.fallDistance = 0.0f;

        if (mc.getNetworkHandler() != null) {
            sendingCustom = true;
            try {
                // main position + rotation
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(next.x, next.y, next.z, mc.player.getYaw(), mc.player.getPitch(), false, false));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(next.x, next.y - 42069.0, next.z, true, false));
            } finally {
                sendingCustom = false;
            }
        }
    }

    @EventHandler
    private void onSend(PacketEvent.Send event) {
        if (!sendingCustom && event.packet instanceof PlayerMoveC2SPacket) event.cancel(); // replace
    }

    @EventHandler
    private void onReceive(PacketEvent.Receive event) {
        if (event.packet instanceof CloseScreenS2CPacket) event.cancel(); // SPacketCloseWindow cancel
    }
}
