package thefellas.safepoint.ui.hud.hudcomponents;

import net.minecraft.client.Minecraft;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.ui.hud.HudModule;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class HudArrayListComponent extends HudModule {
    int dragX;
    int dragY;
    boolean isDragging;

    public HudArrayListComponent() {
        super("ArrayList");
        renderX = 0;
        renderY = 10;
    }

    public void dragScreen(int mouseX, int mouseY) {
        if (!isDragging)
            return;
        renderX = dragX + mouseX;
        renderY = dragY + mouseY;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        dragScreen(mouseX, mouseY);
        if (getValue()) {
            if (isInsideDragField(mouseX, mouseY)) {
                RenderUtil.drawRect(renderX, renderY+2, renderX + Safepoint.mc.fontRenderer.getStringWidth("Chat Notifications"), renderY + Safepoint.mc.fontRenderer.FONT_HEIGHT*Safepoint.moduleInitializer.getModuleList().size(), new Color(0, 0, 0, 100).getRGB());
                RenderUtil.drawRect(renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club") + 3, renderY - 7, renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club") + 3 + Safepoint.mc.fontRenderer.getStringWidth("renderX: " + renderX + " renderY: " + renderY), renderY - 7 + Safepoint.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                Safepoint.mc.fontRenderer.drawStringWithShadow("renderX: " + renderX + " renderY: " + renderY, renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club") + 3, renderY - 7, -1);
            }
            drawText();
        }
    }

    public void drawText() {
        int width = 75;
        int height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        int yOffset = 3;
        final ArrayList<Module> enabled = (ArrayList<Module>) Safepoint.moduleInitializer.getEnabledModules();
        if(renderY >= 93){
            enabled.sort(Comparator.comparing(m -> (int)Minecraft.getMinecraft().fontRenderer.getStringWidth(((Module)m).getName())));
        }else{
            enabled.sort(Comparator.comparing(m -> (int)Minecraft.getMinecraft().fontRenderer.getStringWidth(((Module)m).getName())).reversed());
        }
        for(Module module : enabled) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName(), renderX, renderY + yOffset, AC_ClickGui.getInstance().color.getColor().getRGB());
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }
    }
    public boolean isInsideDragField(int mouseX, int mouseY) {
        return (mouseX > renderX && mouseX < renderX + Safepoint.mc.fontRenderer.getStringWidth("Chat Notifications")) && (mouseY > renderY && mouseY < renderY + Safepoint.mc.fontRenderer.FONT_HEIGHT*Safepoint.moduleInitializer.getModuleList().size());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInsideDragField(mouseX, mouseY)) {
            dragX = renderX - mouseX;
            dragY = renderY - mouseY;
            isDragging = true;
        }
        if (mouseButton == 0 && isInside(mouseX, mouseY))
            value = !value;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0)
            isDragging = false;
    }
}
