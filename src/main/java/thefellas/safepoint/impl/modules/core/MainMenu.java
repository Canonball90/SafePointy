package thefellas.safepoint.impl.modules.core;

import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.ColorSetting;
import thefellas.safepoint.impl.settings.impl.EnumSetting;
import thefellas.safepoint.impl.settings.impl.StringSetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "MainMenu", description = "MainMenu", category = Module.Category.Core)
public class MainMenu extends Module {
    static MainMenu INSTANCE = new MainMenu();
    public StringSetting name = new StringSetting("Name", "Safepoint", this);
    public EnumSetting mode = new EnumSetting("Mode", "Gradient", Arrays.asList("Gradient", "Minecraft", "Custom?", "Solid"), this);
    public BooleanSetting particlese = new BooleanSetting("Particles", true, this);
    public ColorSetting color1 = new ColorSetting("Color Top", new Color(255, 0, 0, 255), this);
    public ColorSetting color2 = new ColorSetting("Color Bottom", new Color(72, 0, 255, 255), this);
    public ColorSetting colorTitle = new ColorSetting("Title Color", new Color(255, 255, 255, 255), this);
    public ColorSetting color = new ColorSetting("Color Top", new Color(255, 0, 0, 255), this);

    @Override
    public void initializeModule() {
        setInstance();
    }


    public static MainMenu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainMenu();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

}
