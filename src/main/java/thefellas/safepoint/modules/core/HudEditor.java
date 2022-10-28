package thefellas.safepoint.modules.core;

import thefellas.safepoint.hud.HudWindow;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Hud Editor", category = Module.Category.Core, description = "Edits the hud ye")
public class HudEditor extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(HudWindow.getInstance());
        disableModule();
    }
}
