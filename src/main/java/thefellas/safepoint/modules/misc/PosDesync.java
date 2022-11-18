package thefellas.safepoint.modules.misc;

import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.event.events.SendPacketEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "PosDesync", description = "Desyncs your position", category = Module.Category.Misc)
public class PosDesync extends Module {
    @SubscribeEvent
    public void onPacketSend(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation || event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}
