package thefellas.safepoint.impl.modules.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "NoFall", description = "Negates fall damage",category = Module.Category.Movement)
public class AE_NoFall extends Module {

    @SubscribeEvent
    public void OnUpdate(TickEvent.PlayerTickEvent e) {
        if(nullCheck()) return;
        if (mc.player.fallDistance >= 3) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
            mc.player.fallDistance = 0f;
        }
    }
}
