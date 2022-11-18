package thefellas.safepoint.modules.combat;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.IntegerSetting;

@ModuleInfo(name = "AutoLog", category = Module.Category.Combat, description = "Automatically logs you out when you die")
public class AutoLog extends Module {
    IntegerSetting health = new IntegerSetting("Health", 10, 1, 20, this);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (nullCheck()) return;

        if (mc.player.getHealth() <= health.getValue())
        {
            disableModule();
            mc.world.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
