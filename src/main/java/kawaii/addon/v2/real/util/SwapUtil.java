package kawaii.addon.v2.real.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.item.Item;

public class SwapUtil {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private static int savedSlot = -1;

    public static int findInHotbar(TagKey<Item> tag) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isIn(tag)) return i;
        }
        return -1;
    }

    public static int findInHotbar(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) return i;
        }
        return -1;
    }

    public static void swapSilent(int slot) {
        if (slot == -1) return;
        savedSlot = mc.player.getInventory().getSelectedSlot();
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static void swapBack() {
        if (savedSlot == -1) return;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(savedSlot));
        savedSlot = -1;
    }

    public static void swapNormal(int slot) {
        if (slot == -1) return;
        savedSlot = mc.player.getInventory().getSelectedSlot();
        mc.player.getInventory().setSelectedSlot(slot);
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static void swapBackNormal() {
        if (savedSlot == -1) return;
        mc.player.getInventory().setSelectedSlot(savedSlot);
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(savedSlot));
        savedSlot = -1;
    }

    public static int getSavedSlot() {
        return savedSlot;
    }

    public static boolean isSwapped() {
        return savedSlot != -1;
    }
}
