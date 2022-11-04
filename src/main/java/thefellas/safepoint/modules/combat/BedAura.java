package thefellas.safepoint.modules.combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;

import java.util.Comparator;

@ModuleInfo(name = "BedAura", category = Module.Category.Combat, description = "Automatically places and breaks beds.")
public class BedAura extends Module {
    boolean moving;
    IntegerSetting range = new IntegerSetting("Range", 4, 0, 6, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", true, this);
    BooleanSetting dimensionCheck = new BooleanSetting("DimensionCheck", true, this);
    BooleanSetting refill = new BooleanSetting("Ref", true, this);

    public BedAura() {
        this.moving = false;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.mc.player == null) {
            return;
        }
        if (this.refill.getValue()) {
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                if (this.mc.player.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
                    slot = i;
                    break;
                }
            }
            if (this.moving && slot != -1) {
                this.mc.playerController.windowClick(0, slot + 36, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                this.moving = false;
                slot = -1;
            }
            if (slot != -1 && !(this.mc.currentScreen instanceof GuiContainer) && this.mc.player.inventory.getItemStack().isEmpty()) {
                int t = -1;
                for (int j = 0; j < 45; ++j) {
                    if (this.mc.player.inventory.getStackInSlot(j).getItem() == Items.BED && j >= 9) {
                        t = j;
                        break;
                    }
                }
                if (t != -1) {
                    this.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                    this.moving = true;
                }
            }
        }
        this.mc.world.loadedTileEntityList.stream().filter(e -> e instanceof TileEntityBed).filter(e -> this.mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()) <= this.range.getValue()).sorted(Comparator.comparing(e -> this.mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()))).forEach(bed -> {
            if (!this.dimensionCheck.getValue() || this.mc.player.dimension != 0) {
                this.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
        });
    }
}
