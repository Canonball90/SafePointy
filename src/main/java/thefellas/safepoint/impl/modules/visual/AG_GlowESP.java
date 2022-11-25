package thefellas.safepoint.impl.modules.visual;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "GlowESP", description = "GlowESP hack", category = Module.Category.Visual)
public class AG_GlowESP extends Module {
    private static List<Entity> glowed = new ArrayList<>();
    @Override
    public void onWorldRender() {
        if(nullCheck()) return;
        if (!this.isEnabled()) return;
        for (EntityPlayer playerEntity : mc.world.playerEntities) {
            if (playerEntity != mc.player && playerEntity != glowed) {
                playerEntity.setGlowing(true);
                glowed.add(playerEntity);
            }
        }
    }
    @Override
    public void onDisable(){
        for (Entity entity : glowed) {
            entity.setGlowing(false);
        }
        glowed.clear();
    }
}
