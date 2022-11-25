package thefellas.safepoint.impl.modules.core;

import thefellas.safepoint.impl.ui.clickgui.stats.StatModule;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "Stats", category = Module.Category.Core, description = "Shows the stats of the client")
public class AD_Stats extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new StatModule());
        disableModule();
    }
}
