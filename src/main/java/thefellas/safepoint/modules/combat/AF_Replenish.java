package thefellas.safepoint.modules.combat;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.FloatSetting;

@ModuleInfo(name = "Replenish", description = "Automatically replenishes your health", category = Module.Category.Combat)
public class AF_Replenish extends Module {

    FloatSetting percent = new FloatSetting("Percent", 50f, 1f, 100.0f,this);
    BooleanSetting inventorySpoof = new BooleanSetting("Inventory Spoof", true, this);

    public void onTick() {
        if (nullCheck()) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            double stackPercent = ((double) stack.getCount() / (double) stack.getMaxStackSize()) * 100.0;
            if (stackPercent <= percent.getValue().intValue()) {
                mergeStack(i, stack);
            }
        }
    }

    public void mergeStack(int current, ItemStack stack) {
        int replaceSlot = -1;

        for (int i = 9; i < 36; i++) {
            ItemStack inventoryStack = mc.player.inventory.getStackInSlot(i);

            if (inventoryStack.isEmpty()) {
                continue;
            }
            if (!inventoryStack.getDisplayName().equals(stack.getDisplayName())) {
                continue;
            }
            if (stack.getItem() instanceof ItemBlock && inventoryStack.getItem() instanceof ItemBlock) {
                if (!((ItemBlock) stack.getItem()).getBlock().equals(((ItemBlock) inventoryStack.getItem()).getBlock())) {
                    continue;
                }
            } else {
                if (!stack.getItem().equals(inventoryStack.getItem())) {
                    continue;
                }
            }

            replaceSlot = i;
            break;
        }

        if (replaceSlot != -1) {
            mc.playerController.windowClick(0, replaceSlot, 0, ClickType.PICKUP, mc.player);

            mc.playerController.windowClick(0, current < 9 ? current + 36 : current, 0, ClickType.PICKUP, mc.player);

            mc.playerController.windowClick(0, replaceSlot, 0, ClickType.PICKUP, mc.player);
        }
    }


}
