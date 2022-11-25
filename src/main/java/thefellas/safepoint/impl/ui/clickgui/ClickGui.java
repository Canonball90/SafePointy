package thefellas.safepoint.impl.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.core.AC_ClickGui;
import thefellas.safepoint.impl.ui.clickgui.windows.Window;
import thefellas.safepoint.impl.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import thefellas.safepoint.core.utils.RenderUtil;
import thefellas.safepoint.core.utils.particle.neww.ParticleSystem;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {
    static ClickGui INSTANCE = new ClickGui();
    ArrayList<Window> windows = new ArrayList<>();
    private ParticleSystem particleSystem;

    public ClickGui() {
        setInstance();
        load();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private final ResourceLocation neku = new ResourceLocation("owo.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(AC_ClickGui.getInstance().background.getValue()) {
            RenderUtil.drawGradient(0, 0, width, height, new Color(AC_ClickGui.getInstance().backgroundColor.getValue().getRed(), AC_ClickGui.getInstance().backgroundColor.getValue().getGreen(), AC_ClickGui.getInstance().backgroundColor.getValue().getBlue(), 130).getRGB(), new Color(AC_ClickGui.getInstance().color.getValue().getRed(), AC_ClickGui.getInstance().color.getValue().getGreen(), AC_ClickGui.getInstance().color.getValue().getBlue(), 130).getRGB());
            if (this.particleSystem != null && AC_ClickGui.getInstance().particles.getValue()) {
                if(mc.currentScreen != null) {
                    this.particleSystem.render(mouseX, mouseY);
                }
            }
            else {
                this.particleSystem = new ParticleSystem(new ScaledResolution(this.mc));
            }
            int divi=6;
            if(AC_ClickGui.getInstance().uwu.getValue()){
                mc.renderEngine.bindTexture(neku);
                drawScaledCustomSizeModalRect(Minecraft.getMinecraft().currentScreen.width - (1189 / divi), Minecraft.getMinecraft().currentScreen.height - (1620 / divi), 0, 0, 1189 / divi, 1620 / divi, 1189 / divi, 1620 / divi, 1189 / divi, 1620 / divi);
            }
        }
        //RenderUtil.drawGradient(0, 0, width, height, new Color(211, 0, 255, 128).getRGB(), new Color(255, 0, 58, 128).getRGB());
        windows.forEach(windows -> windows.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        windows.forEach(windows -> windows.mouseClicked(mouseX, mouseY, clickedButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releasedButton) {
        windows.forEach(windows -> windows.mouseReleased(mouseX, mouseY, releasedButton));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        windows.forEach(windows -> windows.onKeyTyped(typedChar, keyCode));
    }

    public void updateScreen() {
        if (this.particleSystem != null) {
            this.particleSystem.update();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        windows.forEach(Window::initGui);
    }

    public void load() {
        int x = -130;
        assert Safepoint.moduleInitializer != null;
        for (Module.Category categories : Safepoint.moduleInitializer.getCategories())
            windows.add(new Window(categories.toString(), x += 134, 13, AC_ClickGui.getInstance().width.getValue(), 10, categories));//130 width
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
