package thefellas.safepoint.impl.modules.core;

import thefellas.safepoint.impl.ui.hud.HudWindow;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "Hud Editor", category = Module.Category.Core, description = "Edits the hud ye")
public class AB_HudEditor extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(HudWindow.getInstance());
        disableModule();
    }
}
