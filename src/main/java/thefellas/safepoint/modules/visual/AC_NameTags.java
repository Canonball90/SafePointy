package thefellas.safepoint.modules.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "NameTags", description = "Renders nametags above players", category = Module.Category.Visual)
public class AC_NameTags extends Module {

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

        EntityRenderer.drawNameplate(mc.fontRenderer, entity.getDisplayName().getFormattedText() + ChatFormatting.RED + " \u2764 " + health, 0, 0, 0, 0,
                mc.getRenderManager().playerViewY,
                mc.getRenderManager().playerViewX,
                mc.gameSettings.thirdPersonView == 2, false
        );
        GL11.glPopMatrix();
        e.setCanceled(true);
    }
}
