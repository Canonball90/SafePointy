package thefellas.safepoint.impl.modules.player;

import net.minecraft.client.gui.GuiGameOver;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "Godmode", description = "Godmode", category = Module.Category.Player)
public class AB_Godmode extends Module {
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
