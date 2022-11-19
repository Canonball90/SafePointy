package thefellas.safepoint.ui.menu;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.SafepointMod;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.ui.clickgui.ClickGui;
import thefellas.safepoint.utils.RenderUtil;
import thefellas.safepoint.utils.particle.neww.ParticleSystem;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TutorialMenu extends GuiScreen {
    private static final ResourceLocation texture = new ResourceLocation("texture.jpg");
    private ParticleSystem particleSystem;

    public TutorialMenu() {
        super();
    }

    @Override
    public void initGui() {
        int i = this.height / 4 + 48;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, i + 72 + 12, 98,
                20, "Options"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 2, i + 72 + 12, 98,
                20, "Quit"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, i + 72 - 12, 98,
                20, "ClickGui"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, i + 72 - 12, 98,
                20, "Author"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, i + 72 - 34, 200,
                20, "Multiplayer"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, i + 72 - 58, 200,
                20, "Singleplayer"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        } if (button.id == 1) {
            mc.shutdown();
        } if (button.id == 2) {
            mc.displayGuiScreen(ClickGui.getInstance());
        } if (button.id == 3) {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI("https://www.youtube.com/c/bushroot01"));
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } if (button.id == 4) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        } if (button.id == 5) {
            mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableCull( );
        RenderUtil.drawGradient(0, 0, this.width, this.height, new Color(255, 0, 0, 255).getRGB(), new Color(85, 0, 255, 255).getRGB());
        if (this.particleSystem != null) {
            //if(mc.currentScreen != null) {
                this.particleSystem.render(mouseX, mouseY);
            //}
        }
        else {
            this.particleSystem = new ParticleSystem(new ScaledResolution(this.mc));
        }
        RenderUtil.drawString(5, "Safepoint", this.width / 10 - this.fontRenderer.getStringWidth("Safepoint") / 2,
                this.height / 20, new Color(0, 119, 255).getRGB());
        mc.fontRenderer.drawString("ver:" + SafepointMod.VERSION, 3, 3, new Color(0, 119, 255).getRGB());
        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}