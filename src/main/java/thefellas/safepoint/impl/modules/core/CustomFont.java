package thefellas.safepoint.impl.modules.core;

import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;

@ModuleInfo(name = "CustomFont", description = "Changes the font of the client", category = Module.Category.Core)
public class CustomFont extends Module {

    public BooleanSetting antiAlias = new BooleanSetting("AntiAlias", true, this);
    public BooleanSetting fractional = new BooleanSetting("Fractional Metrics", false, this);

    public static CustomFont INSTANCE;

    public CustomFont() {
        INSTANCE = this;
    }
}

