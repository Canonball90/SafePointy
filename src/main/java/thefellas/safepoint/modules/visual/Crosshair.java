package thefellas.safepoint.modules.visual;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.ColorSetting;

import java.awt.*;

@ModuleInfo(name = "Crosshair", description = "Draws a crosshair on your screen", category = Module.Category.Visual)
public class Crosshair extends Module {

    ColorSetting color = new ColorSetting("Color", new Color(255, 255, 255), this);

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && mc.gameSettings.thirdPersonView == 0)
        {
            ScaledResolution sr = new ScaledResolution(mc);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5, 1.5, 1.5);
            Gui.drawRect(sr.getScaledWidth() / 3 - 4, sr.getScaledHeight() / 3 + 6, sr.getScaledWidth() / 3 + 5, sr.getScaledHeight() / 3 + 7, new Color(1,1,1,50).getRGB());
            Gui.drawRect(sr.getScaledWidth() / 3, sr.getScaledHeight() / 3 - 3, sr.getScaledWidth() / 3 + 1, sr.getScaledHeight() / 3 + 4, new Color(color.getValue().getRGB()).getRGB());
            Gui.drawRect(sr.getScaledWidth() / 3 - 3, sr.getScaledHeight() / 3 - 1, sr.getScaledWidth() / 3 + 4, sr.getScaledHeight() / 3 + 1, new Color(color.getValue().getRGB()).getRGB());
            GlStateManager.popMatrix();
        }
    }
}
