package thefellas.safepoint.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.*;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "Chams", description = "", category = Module.Category.Visual)
public class Chams extends Module {

    ColorSetting color = new ColorSetting("Color", new Color(255, 2, 2, 2), this);
    EnumSetting mode = new EnumSetting("Mode", "XYZ", Arrays.asList("Flat", "XYZ", "Texture", "Color"), this);
    BooleanSetting lines = new BooleanSetting("Lines", true, this);
    IntegerSetting width = new IntegerSetting("Width", 40, 0, 100, this);

    BooleanSetting pulse = new BooleanSetting("Pulse", true, this);
    FloatSetting pulseMax = new FloatSetting("Pulse Max", 1.5f, 0.0f, 255.0f, this);
    FloatSetting pulseMin = new FloatSetting("Pulse Min", 1.0f, 0.0f, 255.0f, this);
    FloatSetting pulseSpeed = new FloatSetting("Pulse Speed", 4.0f, 0.0f, 5.0f, this);
    FloatSetting rollingWidth = new FloatSetting("Pulse W", 8.0f, 0.0f, 20.0f, this);

    @SubscribeEvent
    public void onRenderLiving(RenderPlayerEvent.Pre event) {
        final Color c = color.getValue();
        GL11.glPushMatrix();
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1.0E7f);
        GL11.glPushAttrib(1048575);
        if (!this.lines.getValue()) {
            GL11.glPolygonMode(1028, 6914);
        } else {
            GL11.glPolygonMode(1028, 6913);
        }
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (mode.getValue().equalsIgnoreCase("Flat")) {
            GL11.glColor3f(color.getValue().getRed() / 255.0f, color.getValue().getGreen() / 255.0f, color.getValue().getBlue() / 255.0f);
        }else {
            if(pulse.getValue()){ GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f,  getRolledHeight(4) / 255.0f / 2.0f);}
            else GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f / 2.0f);
        }
        if (this.lines.getValue()) {
            GL11.glLineWidth(this.width.getValue() / 10.0f);
        }
    }

    @SubscribeEvent
    public void onRenderLivingPost(RenderPlayerEvent.Post event) {
        GL11.glPopAttrib();
        GL11.glPolygonOffset(1.0f, 1.0E7f);
        GL11.glDisable(32823);
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void c(RenderLivingEvent.Pre e){
        final Color c = new Color(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue());
        GL11.glPushMatrix();
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1.0E7f);
        GL11.glPushAttrib(1048575);
        if (!this.lines.getValue()) {
            GL11.glPolygonMode(1028, 6914);
        } else {
            GL11.glPolygonMode(1028, 6913);
        }
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (mode.getValue().equalsIgnoreCase("Flat")) {
            GL11.glColor3f(color.getValue().getRed() / 255.0f, color.getValue().getGreen() / 255.0f, color.getValue().getBlue() / 255.0f);
        }else {
            GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, color.getValue().getAlpha() / 255.0f / 2.0f);
        }
        if (this.lines.getValue()) {
            GL11.glLineWidth(this.width.getValue() / 10.0f);
        }
    }

    @SubscribeEvent
    public void c(RenderLivingEvent.Post event) {
        GL11.glPopAttrib();
        GL11.glPolygonOffset(1.0f, 1.0E7f);
        GL11.glDisable(32823);
        GL11.glPopMatrix();
    }

    private float getRolledHeight(float offset) {
        double s = (System.currentTimeMillis() * (double)pulseSpeed.getValue()) + (offset * rollingWidth.getValue() * 100.0f);
        s %= 300.0;
        s = (150.0f * Math.sin(((s - 75.0f) * Math.PI) / 150.0f)) + 150.0f;
        return pulseMax.getValue() + ((float)s * ((pulseMin.getValue() - pulseMax.getValue()) / 300.0f));
    }
}
