package thefellas.safepoint.modules.visual;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import thefellas.safepoint.modules.*;
import thefellas.safepoint.settings.impl.EnumSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ModuleInfo(name = "FullBright", description = "Easier to see now haha", category = Module.Category.Visual)
public class AD_FullBright extends Module {

    private float oldBright;

    EnumSetting renderMode = new EnumSetting("RenderMode", "Gamma", Arrays.asList("Gamma", "Potion"), this);

    @Override
    public void onEnable() {

        if(renderMode.getValue().equalsIgnoreCase("Gamma")) {
            oldBright = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 10f;
        }else if(renderMode.getValue().equalsIgnoreCase("Potion")) {
            mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 999999, 1));
        }
    }

    @Override
    public void onDisable() {
        if(renderMode.getValue().equalsIgnoreCase("Gamma")) {
            mc.gameSettings.gammaSetting = oldBright;
        }else if(renderMode.getValue().equalsIgnoreCase("Potion")) {
            mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
        }
    }

}
