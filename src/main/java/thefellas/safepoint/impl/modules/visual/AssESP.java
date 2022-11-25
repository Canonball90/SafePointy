package thefellas.safepoint.impl.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.FloatSetting;

@ModuleInfo(name = "AssESP", description = "Shows the name of players through walls", category = Module.Category.Visual)
public class AssESP extends Module {

    FloatSetting yOffset = new FloatSetting("Y Offset", 0, -10.0F, 10.0F, this);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for (Object e : mc.world.loadedEntityList) {
            if (!(e instanceof EntityPlayer) || e == mc.player) continue;
            RenderManager renderManager = Minecraft.getMinecraft ( ).getRenderManager ( );
            EntityPlayer entityPlayer = (EntityPlayer) e;
            double x = ((EntityPlayer) e).lastTickPosX + (((EntityPlayer) e).posX - ((EntityPlayer) e).lastTickPosX) * (double)event.getPartialTicks() - NameTags.mc.getRenderManager().viewerPosX;
            double y = ((EntityPlayer) e).lastTickPosY + (((EntityPlayer) e).posY - ((EntityPlayer) e).lastTickPosY) * (double)event.getPartialTicks() - NameTags.mc.getRenderManager().viewerPosY - yOffset.getValue();
            double z = ((EntityPlayer) e).lastTickPosZ + (((EntityPlayer) e).posZ - ((EntityPlayer) e).lastTickPosZ) * (double)event.getPartialTicks() - NameTags.mc.getRenderManager().viewerPosZ;
            GL11.glPushMatrix ( );
            RenderHelper.disableStandardItemLighting ( );
            this.esp ( entityPlayer , x , y , z );
            RenderHelper.enableStandardItemLighting ( );
            GL11.glPopMatrix ( );
        }
    }

    public
    void esp ( EntityPlayer entityPlayer , double d , double d2 , double d3 ) {
        GL11.glDisable ( 2896 );
        GL11.glDisable ( 3553 );
        GL11.glEnable ( 3042 );
        GL11.glBlendFunc ( 770 , 771 );
        GL11.glDisable ( 2929 );
        GL11.glEnable ( 2848 );
        GL11.glDepthMask ( true );
        GL11.glLineWidth ( 1.0f );
        GL11.glTranslated ( d , d2 , d3 );
        GL11.glRotatef ( - entityPlayer.rotationYaw , 0.0f , entityPlayer.height , 0.0f );
        GL11.glTranslated ( - d , - d2 , - d3 );
        GL11.glTranslated ( d , d2 + (double) ( entityPlayer.height / 2.0f ) - (double) 0.225f , d3 );
        GL11.glColor4f ( (255 / 255.0f) , (160 / 255.0f) , (90 / 255.0f) , 1.0f );
        GL11.glRotated ( ( entityPlayer.isSneaking ( ) ? 35 : 0) , 1.0f, 0.0, 0);
        Sphere sphere = new Sphere( );
        GL11.glTranslated ( -0.15 , 0.0 , -0.2);
        sphere.setDrawStyle ( 100013 );
        sphere.draw ( 0.20f , 10 , 20 );
        GL11.glTranslated ( 0.35000000149011612 , 0.0 , 0.0 );
        Sphere sphere3 = new Sphere ( );
        sphere3.setDrawStyle ( 100013 );
        sphere3.draw ( 0.20f , 15 , 20 );
        GL11.glDepthMask ( true );
        GL11.glDisable ( 2848 );
        GL11.glEnable ( 2929 );
        GL11.glDisable ( 3042 );
        GL11.glEnable ( 2896 );
        GL11.glEnable ( 3553 );
    }
}
