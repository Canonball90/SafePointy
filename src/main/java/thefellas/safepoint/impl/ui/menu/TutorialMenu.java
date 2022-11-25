package thefellas.safepoint.impl.ui.menu;

import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import thefellas.safepoint.SafepointMod;
import thefellas.safepoint.impl.modules.core.MainMenu;
import thefellas.safepoint.impl.ui.clickgui.ClickGui;
import thefellas.safepoint.core.utils.RenderUtil;
import thefellas.safepoint.core.utils.particle.neww.ParticleSystem;

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
        this.buttonList.add(new GuiButton(6, this.width / 2, i + 300, 98,
                20, "5b5t.org"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 100, i + 300, 98,
                20, "2b2t.org"));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 200, i + 300, 98,
                20, "2b2tpvp.org"));
        this.buttonList.add(new GuiButton(9, this.width / 2 - 300, i + 300, 98,
                20, "strict.2b2tpvp.org"));
        this.buttonList.add(new GuiButton(10, this.width / 2 - 400, i + 300, 98,
                20, "crystalpvp.cc"));
        this.buttonList.add(new GuiButton(11, this.width / 2 + 100, i + 300, 98,
                20, "0b0t.org"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }if (button.id == 1) {
            mc.shutdown();
        }if (button.id == 2) {
            mc.displayGuiScreen(ClickGui.getInstance());
        }if (button.id == 3) {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI("https://www.pornhub.com"));
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }if (button.id == 4) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }if (button.id == 5) {
            mc.displayGuiScreen(new GuiWorldSelection(this));
        }if (button.id == 6) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "5b5t.org", 25565));
        }if (button.id == 7) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "2b2t.org", 25565));
        }if (button.id == 8) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "2b2tpvp.org", 25565));
        }if (button.id == 9) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "strict.2b2tpvp.org", 25565));
        }if (button.id == 10) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "crystalpvp.cc", 25565));
        }if (button.id == 11) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "0b0t.org", 25565));
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableCull( );
        if(MainMenu.getInstance().mode.getValue().equalsIgnoreCase("Gradient")) {
            RenderUtil.drawGradient(0, 0, this.width, this.height, MainMenu.getInstance().color1.getValue().getRGB(),  MainMenu.getInstance().color2.getValue().getRGB());
        }
        if(MainMenu.getInstance().mode.getValue().equalsIgnoreCase("Custom?")) {
            RenderUtil.drawGradient(0, 0, this.width, this.height,  MainMenu.getInstance().color1.getValue().getRGB(),  MainMenu.getInstance().color2.getValue().getRGB());
        }
        if(MainMenu.getInstance().mode.getValue().equalsIgnoreCase("Solid")) {
            RenderUtil.drawRect(0, 0, this.width, this.height, MainMenu.getInstance().color.getValue().getRGB());
        }
        if(MainMenu.getInstance().mode.getValue().equalsIgnoreCase("Minecraft")) {
            drawDefaultBackground();
        }
        if(MainMenu.getInstance().particlese.getValue()) {
            if (this.particleSystem != null) {
                if(mc.currentScreen != null) {
                this.particleSystem.render(mouseX, mouseY);
                }
            } else {
                this.particleSystem = new ParticleSystem(new ScaledResolution(this.mc));
            }
        }
        RenderUtil.drawString(5, MainMenu.getInstance().name.getValue(), this.width / 10 - this.fontRenderer.getStringWidth(MainMenu.getInstance().name.getValue()) / 2,
                this.height / 20,MainMenu.getInstance().colorTitle.getValue().getRGB());
        mc.fontRenderer.drawString("ver:" + SafepointMod.VERSION, 3, 3, new Color(0, 119, 255).getRGB());

        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        if (this.particleSystem != null) {
            this.particleSystem.update();
        }
        super.updateScreen();
    }
}