package thefellas.safepoint.clickgui.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class StatUtils {

    public static void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8, 8, 8, 8, 37, 37, 64, 64);
    }

    public static int getPlayerKills() {
        return Minecraft.getMinecraft().player.getStatFileWriter().readStat(net.minecraft.stats.StatList.PLAYER_KILLS);
    }

    public static int getPlayerDeaths() {
        return Minecraft.getMinecraft().player.getStatFileWriter().readStat(net.minecraft.stats.StatList.DEATHS);
    }

    public static double getPlayerKD() {
        return (double) getPlayerKills() / (double) getPlayerDeaths();
    }
}
