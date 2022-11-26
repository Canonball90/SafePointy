package thefellas.safepoint.impl.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.FloatSetting;

@ModuleInfo(name = "NoRender",
description = "",
category = Module.Category.Visual)
public class NoRender extends Module {

    BooleanSetting weather = new BooleanSetting("Weather", true, this);
    BooleanSetting bob = new BooleanSetting("Bob", true, this);
    BooleanSetting name = new BooleanSetting("NameTags", true, this);
    FloatSetting yOff = new FloatSetting("yOff", 80,0,90, this, v -> name.getValue());

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;

        if(bob.getValue()) {
            mc.gameSettings.viewBobbing = false;
        }
        if(weather.getValue()){
            if (Minecraft.getMinecraft().world.isRaining()) {
                Minecraft.getMinecraft().world.setRainStrength(0);
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Specials.Pre e) {
        EntityLivingBase entity = e.getEntity();

        if (!(entity instanceof EntityPlayer) || entity == mc.player) {
            return;
        } if (entity.isDead || entity.getHealth() < 0 || entity.isInvisible()) {
            return;
        }

        GL11.glPushMatrix();
        Vec3d pos = new Vec3d(e.getX(), e.getY() + entity.height / 1.5, e.getZ());
        GL11.glTranslated(pos.x, pos.y + 1, pos.z);

        double scale = Math.max(1, pos.distanceTo(new Vec3d(0, 0, 0)) / 6);
        GL11.glScaled(scale, scale, scale);
        int health = (int) Math.ceil(entity.getHealth());

        EntityRenderer.drawNameplate(mc.fontRenderer, entity.getHealth() + "", 0, 0 + yOff.getValue(), 0, 0,
                mc.getRenderManager().playerViewY,
                mc.getRenderManager().playerViewX,
                mc.gameSettings.thirdPersonView == 2, false
        );
        GL11.glPopMatrix();
        e.setCanceled(true);
    }
}
