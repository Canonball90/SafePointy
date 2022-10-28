package thefellas.safepoint.modules.core;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.ColorSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "Click Gui", category = Module.Category.Core, description = "Displays the clickgui.")
public class ClickGui extends Module {
    static ClickGui INSTANCE = new ClickGui();
    public ColorSetting color = new ColorSetting("Color", new Color(255, 255, 255, 255), this);
    public IntegerSetting integerSetting = new IntegerSetting("I", 100, 0, 500, this);
    public BooleanSetting background = new BooleanSetting("Background", true, this);
    public ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(0, 0, 0, 50), this,  v -> background.getValue());
    public ColorSetting backgroundColor2 = new ColorSetting("Background Color 2", new Color(0, 0, 0, 50), this,  v -> background.getValue());

    @Override
    public void initializeModule() {
        setInstance();
        setKeyBind(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(thefellas.safepoint.clickgui.ClickGui.getInstance());
    }

    @Override
    public void onDisable() {
        Safepoint.configInitializer.save();
    }

    @Override
    public void onTick() {
        if (!(mc.currentScreen instanceof thefellas.safepoint.clickgui.ClickGui) && isEnabled())
            disableModule();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

}
