package thefellas.safepoint.modules.combat;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.EnumSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;

import java.util.Arrays;

@ModuleInfo(name = "Offhand", description = "Automatically switches to a better item in your offhand", category = Module.Category.Combat)
public class Offhand extends Module {
    IntegerSetting health = new IntegerSetting("Health", 10, 1, 20, this);
    EnumSetting mode = new EnumSetting("Mode", "Crystal", Arrays.asList("Crystal", "Totem", "Gap"), this);


    NonNullList<ItemStack> inv;
    int TotemCache;
    int InvID;


    @Override
    public void onEnable(){
        if(nullCheck()) return;
        inv = mc.player.inventory.mainInventory;
        for (InvID = 0; InvID < inv.size(); InvID++) {
            if (inv.get(InvID) != ItemStack.EMPTY) {
                if (inv.get(InvID).getItem() == Items.TOTEM_OF_UNDYING) {
                    TotemCache = InvID;
                    //LoggerUtil.sendMessage("The Next Totem is in inv slot" + Integer.toString(TotemCache));
                    break;
                }
            }
        }
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(nullCheck()) return;
        if (mc.player.getHealth() <= health.getValue() || mode.getValue().equalsIgnoreCase("Totem") ) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING){
                //LoggerUtil.sendMessage("Putting Totem In Offhand!");

                replaceOffHand(TotemCache);


				/* Essentially everything within this block is finding the next totem, We first define the size of the inventory, then loops through it and
				the first totem it finds it adds to the Totem cache. More on that cache later, I put this to only do this After everything else to improve efficiency*/
                inv = mc.player.inventory.mainInventory;
                for (InvID = 0; InvID < inv.size(); InvID++) {
                    if (inv.get(InvID) != ItemStack.EMPTY) {
                        if (inv.get(InvID).getItem() == Items.TOTEM_OF_UNDYING) {
                            TotemCache = InvID;
                            //LoggerUtil.sendMessage("The Next Totem is in inv slot" + Integer.toString(TotemCache));
                            break;
                        }
                    }
                }
            }
        }
        // SWITCH TO GAPPLE IF MODE IS GAPPLE
        else if (mc.player.getHealth() > health.getValue() && mode.getValue().equalsIgnoreCase("Gapple") ) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE){
                //LoggerUtil.sendMessage("Putting Totem In Offhand!");
                inv = mc.player.inventory.mainInventory;
                for (InvID = 0; InvID < inv.size(); InvID++) {
                    if (inv.get(InvID) != ItemStack.EMPTY) {
                        if (inv.get(InvID).getItem() == Items.GOLDEN_APPLE) {
                            replaceOffHand(InvID);
                            break;
                        }
                    }
                }
            }
        }
        //SWITCH TO CRYSTAL
        else if (mc.player.getHealth() > health.getValue() && mode.getValue().equalsIgnoreCase("Crystal") ) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL){
                //LoggerUtil.sendMessage("Putting Totem In Offhand!");
                inv = mc.player.inventory.mainInventory;
                for (InvID = 0; InvID < inv.size(); InvID++) {
                    if (inv.get(InvID) != ItemStack.EMPTY) {
                        if (inv.get(InvID).getItem() == Items.END_CRYSTAL) {
                            replaceOffHand(InvID);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void replaceOffHand(int InvID) {
        if (mc.player.openContainer instanceof ContainerPlayer) {
            mc.playerController.windowClick(0, InvID < 9 ? InvID + 36 : InvID, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, InvID < 9 ? InvID + 36 : InvID, 0, ClickType.PICKUP, mc.player);
        }
    }
}
