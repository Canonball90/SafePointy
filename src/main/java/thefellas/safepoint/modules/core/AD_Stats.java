package thefellas.safepoint.modules.core;

import thefellas.safepoint.clickgui.stats.StatModule;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Stats", category = Module.Category.Core, description = "Shows the stats of the client")
public class AD_Stats extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new StatModule());
        disableModule();
    }
}
