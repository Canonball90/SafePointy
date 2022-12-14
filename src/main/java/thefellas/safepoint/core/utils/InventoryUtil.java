package thefellas.safepoint.core.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import static thefellas.safepoint.core.utils.BlockUtil.mc;

public class InventoryUtil {
    public static int getItemHotbar(Item item) {
        for (int i = 0; i < 9; i++) {
            if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar) {
        for (int i = (hotbar ? 0 : 9); i < 36; i++) {
            if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getTotalAmountOfItem(Item item) {
        int amountOfItem = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack.getItem() == item)
                amountOfItem += stack.getCount();
        }
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item)
            amountOfItem += Minecraft.getMinecraft().player.getHeldItemOffhand().getCount();
        return amountOfItem;
    }

    public static void moveItemToSlot(Integer startSlot, Integer endSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
    }

    public static void silentSwitchToSlot(int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static void switchToSlot(int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().player.inventory.currentItem = slot;
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static int findHotbarBlock(final Class c) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (c.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    if (c.isInstance(((ItemBlock) stack.getItem()).getBlock())) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static void updateSlot(int slot, ItemStack stack) {
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(slot, stack));
    }


    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item)
                slot = i;
        }
        return slot;
    }
}
