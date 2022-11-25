package thefellas.safepoint.impl.modules.core;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "MCF", description = "MCF",category = Module.Category.Core)
public class MCF extends Module {

    public int timer;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (Mouse.isButtonDown((int)2) && (this.timer & 0x3E8) == 0) {
            this.timer += 50;
            Entity friend = MCF.mc.objectMouseOver.entityHit;
            if (friend != null) {
                Safepoint.friendInitializer.addFriend(friend.getName());
            }
        }
        ++this.timer;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}
