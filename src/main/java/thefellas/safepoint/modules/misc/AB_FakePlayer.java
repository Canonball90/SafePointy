package thefellas.safepoint.modules.misc;

import com.mojang.authlib.GameProfile;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import thefellas.safepoint.settings.impl.StringSetting;

import java.util.UUID;

@ModuleInfo(name = "Fake Player", category = Module.Category.Misc, description = "Spawns fake entity")
public class AB_FakePlayer extends Module {
    EntityOtherPlayerMP fake_player;

    StringSetting name = new StringSetting("Name", "Notch", this);

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), name.getValue()));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.inventory = mc.player.inventory;
        fake_player.setHealth(36);
        mc.world.addEntityToWorld(-100, fake_player);
    }

    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (fake_player != null && fake_player.getDistanceSq(mc.player) > (100 * 100))
            mc.world.removeEntityFromWorld(-100);
    }

    @Override
    public void onDisable() {
        if (fake_player != null)
            mc.world.removeEntityFromWorld(-100);
    }
}
