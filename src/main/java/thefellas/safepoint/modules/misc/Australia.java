package thefellas.safepoint.modules.misc;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Australia", description = "Australia", category = Module.Category.Misc)
public class Australia extends Module {

    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/flip.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mc.entityRenderer.getShaderGroup() != null && mc.currentScreen == null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
        mc.player.setFire(1);
    }
}
