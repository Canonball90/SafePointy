package thefellas.safepoint.impl.modules.visual;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.core.utils.RenderUtil;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.ColorSetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.FloatSetting;

import java.awt.*;

@ModuleInfo(name = "Crosshair", description = "Draws a crosshair on your screen", category = Module.Category.Visual)
public class Crosshair extends Module {

    ColorSetting fillColor = new ColorSetting("Fill Color", new Color(255, 255, 255), this);
    ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(255, 255, 255), this);
    FloatSetting length = new FloatSetting("Length", 10.0f, 0.0f, 25.0f, this);
    FloatSetting partWidth = new FloatSetting("Part Width", 2.5f, 0.0f, 25.0f, this);
    FloatSetting gap = new FloatSetting("Gap", 6.1f, 0.0f, 25.0f, this);
    FloatSetting outlineWidth = new FloatSetting("Outline Width", 1.0f, 0.0f, 5.0f, this);
    BooleanSetting dynamic = new BooleanSetting("Dynamic", true, this);
    BooleanSetting attackIndicator = new BooleanSetting("Attack Indicator", true, this);
    BooleanSetting fill = new BooleanSetting("Fill", true, this);
    BooleanSetting outline = new BooleanSetting("Outline", true, this);

    @Override
    public void onEnable() {
        GuiIngameForge.renderCrosshairs = false;
    }

    @Override
    public void onDisable() {
        GuiIngameForge.renderCrosshairs = true;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event)
    {
        final ScaledResolution resolution = new ScaledResolution(Crosshair.mc);
        final float width = resolution.getScaledWidth() / 2.0f;
        final float height = resolution.getScaledHeight() / 2.0f;
        if (this.fill.getValue()) {
            RenderUtil.drawRect(width - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2.0f : 0.0f), height - this.partWidth.getValue() / 2.0f, width - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2.0f : 0.0f) + this.length.getValue(), height - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), this.fillColor.getValue().getRGB());
            RenderUtil.drawRect(width + this.gap.getValue() + (this.isMoving() ? 2 : 0), height - this.partWidth.getValue() / 2.0f, width + this.gap.getValue() + (this.isMoving() ? 2 : 0) + this.length.getValue(), height - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), this.fillColor.getValue().getRGB());
            RenderUtil.drawRect(width - this.partWidth.getValue() / 2.0f, height - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2 : 0), width - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), height - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2 : 0) + this.length.getValue(), this.fillColor.getValue().getRGB());
            RenderUtil.drawRect(width - this.partWidth.getValue() / 2.0f, height + this.gap.getValue() + (this.isMoving() ? 2 : 0), width - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), height + this.gap.getValue() + (this.isMoving() ? 2 : 0) + this.length.getValue(), this.fillColor.getValue().getRGB());
            if (this.attackIndicator.getValue() && Crosshair.mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                RenderUtil.drawRect(width - 10.0f, height + this.gap.getValue() + this.length.getValue() + (this.isMoving() ? 2 : 0) + 2.0f, width - 10.0f + Crosshair.mc.player.getCooledAttackStrength(0.0f) * 20.0f, height + this.gap.getValue() + this.length.getValue() + (this.isMoving() ? 2 : 0) + 2.0f + 2.0f, this.fillColor.getValue().getRGB());
            }
        }
        if (this.outline.getValue()) {
            drawOutline(width - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2.0f : 0.0f), height - this.partWidth.getValue() / 2.0f, width - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2.0f : 0.0f) + this.length.getValue(), height - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), this.outlineWidth.getValue(), this.outlineColor.getValue().getRGB());
            drawOutline(width + this.gap.getValue() + (this.isMoving() ? 2 : 0), height - this.partWidth.getValue() / 2.0f, width + this.gap.getValue() + (this.isMoving() ? 2 : 0) + this.length.getValue(), height - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), this.outlineWidth.getValue(), this.outlineColor.getValue().getRGB());
            drawOutline(width - this.partWidth.getValue() / 2.0f, height - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2 : 0), width - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), height - this.gap.getValue() - this.length.getValue() - (this.isMoving() ? 2 : 0) + this.length.getValue(), this.outlineWidth.getValue(), this.outlineColor.getValue().getRGB());
            drawOutline(width - this.partWidth.getValue() / 2.0f, height + this.gap.getValue() + (this.isMoving() ? 2 : 0), width - this.partWidth.getValue() / 2.0f + this.partWidth.getValue(), height + this.gap.getValue() + (this.isMoving() ? 2 : 0) + this.length.getValue(), this.outlineWidth.getValue(), this.outlineColor.getValue().getRGB());
            if (this.attackIndicator.getValue() && Crosshair.mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                drawOutline(width - 10.0f, height + this.gap.getValue() + this.length.getValue() + (this.isMoving() ? 2 : 0) + 2.0f, width - 10.0f + Crosshair.mc.player.getCooledAttackStrength(0.0f) * 20.0f, height + this.gap.getValue() + this.length.getValue() + (this.isMoving() ? 2 : 0) + 2.0f + 2.0f, this.outlineWidth.getValue(), this.outlineColor.getValue().getRGB());
            }
        }
    }

    public boolean isMoving() {
        return (Crosshair.mc.player.isSneaking() || Crosshair.mc.player.moveStrafing != 0.0f || Crosshair.mc.player.moveForward != 0.0f || !Crosshair.mc.player.onGround) && this.dynamic.getValue();
    }

    public static void drawOutline(float x, float y, float width, float height, float lineWidth, int color) {
        RenderUtil.drawRect(x + lineWidth, y, x - lineWidth, y + lineWidth, color);
        RenderUtil.drawRect(x + lineWidth, y, width - lineWidth, y + lineWidth, color);
        RenderUtil.drawRect(x, y, x + lineWidth, height, color);
        RenderUtil.drawRect(width - lineWidth, y, width, height, color);
        RenderUtil.drawRect(x + lineWidth, height - lineWidth, width - lineWidth, height, color);
    }

}
