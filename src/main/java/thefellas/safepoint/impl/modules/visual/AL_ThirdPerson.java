package thefellas.safepoint.impl.modules.visual;

import org.lwjgl.input.Keyboard;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.KeySetting;

@ModuleInfo(name = "ThirdPerson", description = "Third Person", category = Module.Category.Visual)
public class AL_ThirdPerson extends Module {
    BooleanSetting onlyHold = new BooleanSetting("Only Hold", false, this);
    KeySetting bind = new KeySetting("Bind", 0, this);

    @Override
    public void onTick(){
        if(this.bind.getValue().intValue() > -1) {
            if (Keyboard.isKeyDown(this.bind.getValue())) {
                mc.gameSettings.thirdPersonView = 1;
            } else {
                mc.gameSettings.thirdPersonView = 0;
            }
        }
    }
    @Override
    public void onEnable(){
        if(!this.onlyHold.getValue()){
            mc.gameSettings.thirdPersonView = 1;
        }
    }
    @Override
    public void onDisable(){
        if(!this.onlyHold.getValue()){
            mc.gameSettings.thirdPersonView = 0;
        }
    }
}
