package thefellas.safepoint.impl.modules.player;

import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.FloatSetting;

@ModuleInfo(name = "BigPOV", description = "Changes your height", category = Module.Category.Player)
public class AC_BigPOV extends Module {
    FloatSetting height = new FloatSetting("Speed", 5, 2, 15, this);

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        mc.player.eyeHeight=height.getValue();
        mc.getRenderManager().entityRenderMap.put(EntityPig.class, new RenderPig(mc.getRenderManager()));
    }
    @Override
    public void onDisable() {
        mc.player.eyeHeight = mc.player.getDefaultEyeHeight();
        mc.getRenderManager().entityRenderMap.put(EntityPig.class, new RenderPig(mc.getRenderManager()));
    }
}
