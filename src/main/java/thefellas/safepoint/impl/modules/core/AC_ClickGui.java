package thefellas.safepoint.impl.modules.core;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.*;
import org.lwjgl.input.Keyboard;
import thefellas.safepoint.impl.ui.clickgui2.ClickGui;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "Click Gui", category = Module.Category.Core, description = "Displays the clickgui.")
public class AC_ClickGui extends Module {
    static AC_ClickGui INSTANCE = new AC_ClickGui();
    public EnumSetting mode = new EnumSetting("Mode", "Old", Arrays.asList("New", "Old"), this);
    public ColorSetting color = new ColorSetting("Color", new Color(255, 0, 0, 255), this);
    public IntegerSetting integerSetting = new IntegerSetting("I", 100, 0, 500, this);
    public IntegerSetting width = new IntegerSetting("Width", 130, 90, 200, this);
    public BooleanSetting Sta = new BooleanSetting("Staic", false, this);
    public ParentSetting backParent = new ParentSetting("BackGround", false, this);
    public BooleanSetting background = new BooleanSetting("Background", true, this).setParent(backParent);
    public BooleanSetting particles = new BooleanSetting("Particles", true, this, v -> background.getValue()).setParent(backParent);
    public IntegerSetting partLength = new IntegerSetting("Particle Length", 197, 90, 500, this, v -> background.getValue()).setParent(backParent);
    public BooleanSetting uwu = new BooleanSetting("Uwu", false, this, v -> background.getValue()).setParent(backParent);
    public BooleanSetting blur = new BooleanSetting("Blur", false, this, v -> background.getValue()).setParent(backParent);
    public ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(0, 0, 0, 50), this,  v -> background.getValue()).setParent(backParent);
    public ColorSetting backgroundColor2 = new ColorSetting("Background Color 2", new Color(255, 0, 0, 50), this,  v -> background.getValue()).setParent(backParent);

    @Override
    public void initializeModule() {
        setInstance();
        setKeyBind(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        if(mode.getValue().equalsIgnoreCase("Old")) {
            mc.displayGuiScreen(thefellas.safepoint.impl.ui.clickgui.ClickGui.getInstance());
        } else {
            mc.displayGuiScreen(ClickGui.getInstance());
        }
    }

    @Override
    public void onDisable() {
        if (AC_ClickGui.mc.world != null) {
            AC_ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        Safepoint.configInitializer.save();
    }

    @Override
    public void onTick() {
        if (!(mc.currentScreen instanceof thefellas.safepoint.impl.ui.clickgui.ClickGui) && isEnabled())
            disableModule();
    }

    @SubscribeEvent
    public void update(TickEvent.ClientTickEvent event) {
        if (AC_ClickGui.mc.world != null || blur.getValue()) {
            if (AC_ClickGui.mc.currentScreen == thefellas.safepoint.impl.ui.clickgui.ClickGui.getInstance()) {
                if (OpenGlHelper.shadersSupported && AC_ClickGui.mc.getRenderViewEntity() instanceof EntityPlayer) {
                    if (AC_ClickGui.mc.entityRenderer.getShaderGroup() != null) {
                        AC_ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                    try {
                        AC_ClickGui.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (AC_ClickGui.mc.entityRenderer.getShaderGroup() != null && AC_ClickGui.mc.currentScreen == null) {
                    AC_ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
            else if (AC_ClickGui.mc.entityRenderer.getShaderGroup() != null) {
                AC_ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
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
