package thefellas.safepoint.modules.player;

import net.minecraft.client.gui.GuiGameOver;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Godmode", description = "Godmode", category = Module.Category.Player)
public class Godmode extends Module {
    @Override
    public void onTick(){
        if(nullCheck()) return;
        if (mc.player.getHealth() <= 0.0f) {
            mc.player.setHealth(mc.player.getMaxHealth());
            if (mc.currentScreen instanceof GuiGameOver) {
                mc.currentScreen = null;
            }
        }
    }
}
