package thefellas.safepoint.impl.ui.clickgui.stats;

import net.minecraft.client.gui.GuiScreen;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.core.utils.RenderUtil;

import java.awt.*;
import java.util.Objects;

public class StatModule extends GuiScreen {
    public StatModule() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //box
        RenderUtil.drawOutlineRect(200, 200, 500, 400, new Color(0,0,0,255), 4);
        RenderUtil.drawRect(200, 200, 500, 400, new Color(0,0,0,170).getRGB());
        StatUtils.drawHead(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getLocationSkin(), 210, 210);
        //text
        Safepoint.fontInitializer.draw(mc.player.getName().toString(), 250, 210, new Color(255, 255, 255, 255).getRGB());
        Safepoint.fontInitializer.draw("Kills: " + StatUtils.getPlayerKills(), 360, 383, new Color(255, 255, 255, 255).getRGB());
        Safepoint.fontInitializer.draw("Deaths: " + StatUtils.getPlayerDeaths(), 410, 383, new Color(255, 255, 255, 255).getRGB());
        //text up
        Safepoint.fontInitializer.draw("K/D: " + StatUtils.getPlayerKD(), 210, 250, new Color(255, 255, 255, 255).getRGB());
        Safepoint.fontInitializer.draw("Health: " + mc.player.getHealth(), 210, 260, new Color(255, 255, 255, 255).getRGB());
        Safepoint.fontInitializer.draw("Safepoint.club", 202, 390, new Color(255, 255, 255, 255).getRGB());
        //bars
        RenderUtil.drawRect(410, 375 - (StatUtils.getPlayerDeaths() * 4), 450, 380,new Color(253, 0, 0,255).getRGB());
        RenderUtil.drawRect(360, 375 - (StatUtils.getPlayerKills() * 4), 400, 380,new Color(55, 253, 0,255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
