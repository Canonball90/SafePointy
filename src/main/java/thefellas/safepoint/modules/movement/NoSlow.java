package thefellas.safepoint.modules.movement;

import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "NoSlow", description = "NoSlow", category = Module.Category.Movement)
public class NoSlow extends Module {

    @SubscribeEvent
    public void onMove(InputUpdateEvent event) {
        if(nullCheck())return;
        if (mc.player.isHandActive() && !mc.player.isRiding())
        {
            mc.player.movementInput.moveForward /= 0.2F;
            mc.player.movementInput.moveStrafe /= 0.2F;
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

}
