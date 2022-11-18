package thefellas.safepoint.modules.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.modules.*;
import thefellas.safepoint.settings.impl.*;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
/*
@ModuleInfo(name = "ChinaHat", description = "", category = Module.Category.Visual)
public class ChinaHat extends Module {

    private final ColorSetting topColour = new ColorSetting("Top Colour", new Color(185, 17, 255, 180), this);
    private final ColorSetting bottomColour = new ColorSetting("Bottom Colour",  new Color(185, 17, 255, 180), this);

    // Settings
    private final BooleanSetting firstPerson = new BooleanSetting("First Person", false, this);
    private final BooleanSetting others = new BooleanSetting("Others", true, this);

    @SubscribeEvent
    public void onRender3D(final RenderWorldLastEvent e) {
        // Iterate through all players
        mc.world.playerEntities.forEach(player -> {
            // We don't want to render the hat
            if (player == mc.player && !firstPerson.getValue() && mc.gameSettings.thirdPersonView == 0 || !others.getValue() && player != mc.player) {
                return;
            }

            // Render the hat
            renderHat(player);
        });
    }

    public void renderHat(EntityPlayer player) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_BLEND);
        glShadeModel(GL_SMOOTH);
        GlStateManager.disableCull();
        glBegin(GL_TRIANGLE_STRIP);

        // Get the vector to start drawing the hat
        Vec3d vec = EntityUtil.getInterpolatedPosition(player).add(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY + player.getEyeHeight() + 0.5 + (player.isSneaking() ? -0.2 : 0), -mc.getRenderManager().viewerPosZ);

        // Add vertices for each point
        for (float i = 0; i < Math.PI * 2; i += Math.PI * 4 / 128) {
            // X coord
            double hatX = vec.x + 0.65 * Math.cos(i);

            // Z coord
            double hatZ = vec.z + 0.65 * Math.sin(i);

            // Set bottom colour
            GL11.glColor4f(topColour.getValue().getRed() / 255.0f, topColour.getValue().getGreen() / 255.0f, topColour.getValue().getBlue() / 255.0f, topColour.getValue().getAlpha() / 255.0f);

            // Add bottom point
            glVertex3d(hatX, vec.y - 0.25, hatZ);

            // Set top colour
            GL11.glColor4f(bottomColour.getValue().getRed() / 255.0f, bottomColour.getValue().getGreen() / 255.0f, bottomColour.getValue().getBlue() / 255.0f, bottomColour.getValue().getAlpha() / 255.0f);

            // Add top point
            glVertex3d(vec.x, vec.y, vec.z);
        }

        glEnd();
        glShadeModel(GL_FLAT);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.enableCull();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }
}
 */
