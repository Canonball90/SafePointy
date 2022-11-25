package thefellas.safepoint.impl.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

import java.util.Random;

@ModuleInfo(name = "AutoExcuse", description = "Australia", category = Module.Category.Misc)
public class AutoExcuse extends Module {
    int diedTime;

    @SubscribeEvent
    public void update(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        if (this.diedTime > 0) {
            --this.diedTime;
        }
        if (AutoExcuse.mc.player.isDead) {
            this.diedTime = 500;
        }
        if (!AutoExcuse.mc.player.isDead && this.diedTime > 0) {
            final Random rand = new Random();
            final int randomNum = rand.nextInt(6) + 1;
            if (randomNum == 1) {
                AutoExcuse.mc.player.sendChatMessage("you won because you are a pingplayer :((");
            }
            if (randomNum == 2) {
                AutoExcuse.mc.player.sendChatMessage("i was in my hentai file :(");
            }
            if (randomNum == 3) {
                AutoExcuse.mc.player.sendChatMessage("bro im good i was testing configs :((");
            }
            if (randomNum == 5) {
                AutoExcuse.mc.player.sendChatMessage("im desync :(");
            }
            if (randomNum == 6) {
                AutoExcuse.mc.player.sendChatMessage("youre a cheater :(");
            }
            this.diedTime = 0;
        }
    }
}
