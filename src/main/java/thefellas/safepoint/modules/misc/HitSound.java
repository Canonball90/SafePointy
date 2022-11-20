package thefellas.safepoint.modules.misc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.EnumSetting;
import thefellas.safepoint.utils.SoundUtil;

import java.util.Arrays;

@ModuleInfo(name = "HitSound", description = "Plays a sound when you hit an enemy", category = Module.Category.Misc)
public class HitSound extends Module {

    EnumSetting sounds = new EnumSetting("Sounds", "Neverlose", Arrays.asList("Neverlose", "Metallic", "Hit"), this);

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!event.getEntity().equals((Object)this.mc.player)) {
            return;
        }
        if (this.sounds.getValue().equalsIgnoreCase("Neverlose")) {
            SoundUtil.playSound(SoundUtil.INSTANCE.neverlose);
        }
        if (this.sounds.getValue().equalsIgnoreCase("Metallic")) {
            SoundUtil.playSound(SoundUtil.INSTANCE.metallic);
        }
        if (this.sounds.getValue().equalsIgnoreCase("Hit")) {
            SoundUtil.playSound(SoundUtil.INSTANCE.hitsound);
        }
    }
}
