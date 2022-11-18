package thefellas.safepoint.modules.core;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.clickgui.ClickGui;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "GUIBlur", category = Module.Category.Core, description = "Blurs the GUI")
public class GUIBlur extends Module {

    @Override
    public void onDisable() {
        if (GUIBlur.mc.world != null) {
            GUIBlur.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

@SubscribeEvent
    public void update(TickEvent.ClientTickEvent event) {
        if (GUIBlur.mc.world != null) {
            if (GUIBlur.mc.currentScreen == ClickGui.getInstance()) {
                if (OpenGlHelper.shadersSupported && GUIBlur.mc.getRenderViewEntity() instanceof EntityPlayer) {
                    if (GUIBlur.mc.entityRenderer.getShaderGroup() != null) {
                        GUIBlur.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                    try {
                        GUIBlur.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (GUIBlur.mc.entityRenderer.getShaderGroup() != null && GUIBlur.mc.currentScreen == null) {
                    GUIBlur.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
            else if (GUIBlur.mc.entityRenderer.getShaderGroup() != null) {
                GUIBlur.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }
}
