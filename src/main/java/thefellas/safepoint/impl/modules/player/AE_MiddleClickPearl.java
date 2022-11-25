package thefellas.safepoint.impl.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "MiddleClickPearl", description = "Throws a pearl when you middle click", category = Module.Category.Player)
public class AE_MiddleClickPearl extends Module {
    private boolean clicked;

    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event)
    {
        if (mc.currentScreen == null)
        {
            if (Mouse.isButtonDown(2))
            {
                if (!this.clicked)
                {
                    final RayTraceResult result = mc.objectMouseOver;
                    if (result != null && result.typeOfHit == RayTraceResult.Type.MISS)
                    {
                        final int pearlSlot = findPearlInHotbar(mc);
                        if (pearlSlot != -1) {
                            final int oldSlot = mc.player.inventory.currentItem;
                            mc.player.inventory.currentItem = pearlSlot;
                            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                            mc.player.inventory.currentItem = oldSlot;
                        }
                        else
                        {
                            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.GOLD +  "Can't find pearl in hotbar!"));
                        }
                    }
                }
                this.clicked = true;
            }
            else
            {
                this.clicked = false;
            }
        }
    }

    private boolean isItemStackPearl(final ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ItemEnderPearl;
    }

    private int findPearlInHotbar(final Minecraft mc)
    {
        for (int index = 0; InventoryPlayer.isHotbar(index); index++)
        {
            if (isItemStackPearl(mc.player.inventory.getStackInSlot(index))) return index;
        }
        return -1;
    }
}
