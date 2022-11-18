package thefellas.safepoint.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.client.resources.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.entity.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.entity.player.*;

import java.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import net.minecraftforge.fml.common.eventhandler.*;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.modules.*;
import thefellas.safepoint.settings.impl.*;
import thefellas.safepoint.utils.RenderUtil;

import java.util.Arrays;

@ModuleInfo(name = "NameTags", description = "Shows the name of players through walls", category = Module.Category.Visual)
public class NameTags extends Module {

    BooleanSetting armor = new BooleanSetting("Armor", true, this);
    BooleanSetting items = new BooleanSetting("Items", true, this);
    BooleanSetting heart = new BooleanSetting("Heart", false, this);
    BooleanSetting enchant = new BooleanSetting("Enchantments", false, this);
    BooleanSetting background = new BooleanSetting("Background", true, this);
    ColorSetting backGroundColor = new ColorSetting("Background Color", new Color(0, 0, 0, 181), this, v -> background.getValue());
    BooleanSetting outline = new BooleanSetting("Outline", true, this);
    FloatSetting outlineWidth = new FloatSetting("Outline Width", 1.0f, 1.0f, 3.0f, this, v -> outline.getValue());
    ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(255, 0, 0, 255), this, v -> outline.getValue());
    FloatSetting yOffset = new FloatSetting("Y Offset", 0, -10.0F, 10.0F, this);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for (Entity e : NameTags.mc.world.loadedEntityList) {
            if (!(e instanceof EntityPlayer) || e == NameTags.mc.player) continue;
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - NameTags.mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - NameTags.mc.getRenderManager().viewerPosY - yOffset.getValue();
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - NameTags.mc.getRenderManager().viewerPosZ;
            GL11.glPushMatrix();
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            float size = Math.min(Math.max(1.2f * (NameTags.mc.player.getDistance(e) * 0.15f), 1.25f), 6.0f) * 0.015f;
            GL11.glTranslatef((float)((float)x), (float)((float)y + e.height + 0.6f), (float)((float)z));
            GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(-NameTags.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)NameTags.mc.getRenderManager().playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glScalef((float)(-size), (float)(-size), (float)(-size));
            int health = (int)(((EntityPlayer)e).getHealth() / ((EntityPlayer)e).getMaxHealth() * 100.0f);
            if(background.getValue()) {
                Gui.drawRect((int) (-NameTags.mc.fontRenderer.getStringWidth(e.getName() + " " + health + "%") / 2 - 2), (int) -2, (int) (NameTags.mc.fontRenderer.getStringWidth(e.getName()) / 2 + 16), (int) 10, backGroundColor.getColor().getRGB());}
            if(outline.getValue()){RenderUtil.drawOutlineRect((int)(-NameTags.mc.fontRenderer.getStringWidth(e.getName() + " " + health + "%") / 2 - 2), (int)-2, (int)(NameTags.mc.fontRenderer.getStringWidth(e.getName()) / 2 + 16), (int)10, outlineColor.getColor(), outlineWidth.getValue());}
            NameTags.mc.fontRenderer.drawString(e.getName() + " " + (Object) TextFormatting.GREEN + health + (heart.getValue() ? "\u2764" : "%"), 0 - this.getcenter(e.getName() + " " + (Object) TextFormatting.GREEN + health + "%"), 1, -1);
            int posX = -NameTags.mc.fontRenderer.getStringWidth(e.getName()) / 2 - 8;
            if(items.getValue()) {
                if (Item.getIdFromItem((Item) ((EntityPlayer) e).inventory.getCurrentItem().getItem()) != 0) {
                    NameTags.mc.getRenderItem().zLevel = -100.0f;
                    mc.getRenderItem().renderItemIntoGUI(new ItemStack(((EntityPlayer) e).inventory.getCurrentItem().getItem()), posX - 2, -20);
                    NameTags.mc.getRenderItem().zLevel = 0.0f;
                    int posY = -30;
                    Map enchantments = EnchantmentHelper.getEnchantments((ItemStack) ((EntityPlayer) e).inventory.getCurrentItem());
                    for (Object enchantment : enchantments.keySet()) {
                        if(enchant.getValue()) {
                            int level = EnchantmentHelper.getEnchantmentLevel((Enchantment) enchantment, (ItemStack) ((EntityPlayer) e).inventory.getCurrentItem());
                            NameTags.mc.fontRenderer.drawStringWithShadow(String.valueOf(((Enchantment) enchantment).getName().substring(12).charAt(0)).toUpperCase() + level, (float) (posX + 6 - this.getcenter(String.valueOf(((Enchantment) enchantment).getName().substring(12).charAt(0)).toUpperCase() + level)), (float) posY, -1);
                            posY -= 12;
                        }
                    }
                    posX += 15;
                }
            }
            for (ItemStack item : e.getArmorInventoryList()) {
                if(armor.getValue()) {
                    NameTags.mc.getRenderItem().zLevel = -100.0f;
                    mc.getRenderItem().renderItemIntoGUI(new ItemStack(item.getItem()), posX, -20);
                    NameTags.mc.getRenderItem().zLevel = 0.0f;
                    int posY = -30;
                    Map enchantments = EnchantmentHelper.getEnchantments((ItemStack) item);
                    for (Object enchantment : enchantments.keySet()) {
                        if(enchant.getValue()) {
                            int level = EnchantmentHelper.getEnchantmentLevel((Enchantment) enchantment, (ItemStack) item);
                            NameTags.mc.fontRenderer.drawStringWithShadow(String.valueOf(((Enchantment) enchantment).getName().substring(12).charAt(0)).toUpperCase() + level, (float) (posX + 9 - this.getcenter(((Enchantment) enchantment).getName().substring(12).charAt(0) + level)), (float) posY, -1);
                            posY -= 12;
                        }
                    }
                    posX += 17;
                }
            }
            int gapples = 0;
            if (Item.getIdFromItem((Item) ((EntityPlayer) e).inventory.getCurrentItem().getItem()) == 322) {
                gapples = ((EntityPlayer) e).inventory.getCurrentItem().getCount();
            } else if (Item.getIdFromItem((Item) ((EntityPlayer) e).getHeldItemOffhand().getItem()) == 322) {
                gapples = ((EntityPlayer) e).getHeldItemOffhand().getCount();
            }
            if (gapples > 0) {
                NameTags.mc.getRenderItem().zLevel = -100.0f;
                mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.GOLDEN_APPLE), posX, -20);
                NameTags.mc.getRenderItem().zLevel = 0.0f;
                NameTags.mc.fontRenderer.drawStringWithShadow(String.valueOf(gapples), (float) (posX + 9 - this.getcenter(String.valueOf(gapples))), -30.0f, -1);
            }

            GL11.glEnable((int)2929);
            GL11.glPopMatrix();
        }
    }

    public int getcenter(String text) {
        return NameTags.mc.fontRenderer.getStringWidth(text) / 2;
    }

    public int getcenter(int text) {
        return NameTags.mc.fontRenderer.getStringWidth(String.valueOf(text)) / 2;
    }
}
