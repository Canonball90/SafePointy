package thefellas.safepoint.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Criticals", description = "Automatically deals criticals hit", category = Module.Category.Combat)
public class AB_Criticals extends Module {

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if(nullCheck()) return;
        if (AB_Criticals.mc.player.isInWater() || AB_Criticals.mc.player.isInLava()) {
            return;
        }
        if (AB_Criticals.mc.player.onGround) {
            AB_Criticals.mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(AB_Criticals.mc.player.posX, AB_Criticals.mc.player.posY + 0.1625, AB_Criticals.mc.player.posZ, false));
            AB_Criticals.mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(AB_Criticals.mc.player.posX, AB_Criticals.mc.player.posY, AB_Criticals.mc.player.posZ, false));
            AB_Criticals.mc.player.onCriticalHit(event.getTarget());

        }
    }
}
