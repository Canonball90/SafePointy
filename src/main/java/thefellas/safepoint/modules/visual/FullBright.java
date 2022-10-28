package thefellas.safepoint.modules.visual;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import thefellas.safepoint.modules.*;
import thefellas.safepoint.settings.impl.EnumSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ModuleInfo(name = "FullBright", description = "Easier to see now haha", category = Module.Category.Visual)
public class FullBright extends Module {

    private float oldBright;

    List<String> modes = new ArrayList<>();
    {
        modes.add("Gamma");
        modes.add("Potion");
    }

    EnumSetting renderMode = new EnumSetting("RenderMode", "Gamma", modes, this);

    @Override
    public void onEnable() {

        if(renderMode.getModes().equals("Gamma")) {
            oldBright = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 10f;
        }else if(renderMode.getModes().equals("Potion")) {
            mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 999999, 1));
        }
    }

    @Override
    public void onDisable() {
        if(renderMode.getModes().equals("Gamma")) {
            mc.gameSettings.gammaSetting = oldBright;
        }else if(renderMode.getModes().equals("Potion")) {
            mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
        }
    }

}
