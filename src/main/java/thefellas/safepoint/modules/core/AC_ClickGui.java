package thefellas.safepoint.modules.core;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.ColorSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;
import org.lwjgl.input.Keyboard;
import thefellas.safepoint.settings.impl.ParentSetting;

import java.awt.*;

@ModuleInfo(name = "Click Gui", category = Module.Category.Core, description = "Displays the clickgui.")
public class AC_ClickGui extends Module {
    static AC_ClickGui INSTANCE = new AC_ClickGui();
    public ColorSetting color = new ColorSetting("Color", new Color(255, 0, 0, 255), this);
    public IntegerSetting integerSetting = new IntegerSetting("I", 100, 0, 500, this);
    public IntegerSetting width = new IntegerSetting("Width", 130, 90, 200, this);
    public ParentSetting backParent = new ParentSetting("BackGround", false, this);
    public BooleanSetting background = new BooleanSetting("Background", true, this).setParent(backParent);
    public BooleanSetting particles = new BooleanSetting("Particles", true, this, v -> background.getValue()).setParent(backParent);
    public BooleanSetting uwu = new BooleanSetting("Uwu", false, this, v -> background.getValue()).setParent(backParent);
    public ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(0, 0, 0, 50), this,  v -> background.getValue()).setParent(backParent);
    public ColorSetting backgroundColor2 = new ColorSetting("Background Color 2", new Color(255, 0, 0, 50), this,  v -> background.getValue()).setParent(backParent);

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

    public static AC_ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AC_ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

}
