package thefellas.safepoint.impl.modules.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "Quiver", description = "Shoots good arrows in the air", category = Module.Category.Combat)
public class Quiver extends Module {

    @SubscribeEvent
    public void update(TickEvent.PlayerTickEvent e) {
        if(nullCheck()) return;
        if (Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= 3) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(Quiver.mc.player.getActiveHand()));
            Quiver.mc.player.stopActiveHand();
        }
    }
}
