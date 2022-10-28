package thefellas.safepoint.modules.combat;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.ColorSetting;
import thefellas.safepoint.settings.impl.DoubleSetting;
import thefellas.safepoint.settings.impl.FloatSetting;
import thefellas.safepoint.utils.RenderUtil;


import java.awt.*;

@ModuleInfo(name = "KillAura", description = "Attacks entities around you", category = Module.Category.Combat)
public class KillAura extends Module {

    DoubleSetting range = new DoubleSetting("Range", 4, 1, 6, this);
    FloatSetting lineWidth = new FloatSetting("LineWidth", 1f, 0f, 3f, this);

    BooleanSetting players = new BooleanSetting("Players", true, this);
    BooleanSetting animals = new BooleanSetting("Animals", true, this);
    BooleanSetting mobs = new BooleanSetting("Mobs", true, this);

    ColorSetting color = new ColorSetting("Color", new Color(0, 255, 110, 169), this);

    public EntityLivingBase target = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(target != null) {
            if (target.getDistance(mc.player) >= range.getValue() || target.isDead || !target.isEntityAlive())
                target = null;
        }
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity == mc.player) continue;
            if(entity.isDead || !entity.isEntityAlive()) continue;
            if(entity.getDistance(mc.player) <= range.getValue()) {
                if(mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                    if (entity instanceof EntityPlayer && players.getValue()) {
                        target = (EntityLivingBase) entity;
                        attack(entity);
                    }
                    if (entity instanceof EntityAnimal && animals.getValue()) {
                        target = (EntityLivingBase) entity;
                        attack(entity);
                    }
                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue()) {
                        target = (EntityLivingBase) entity;
                        attack(entity);
                    }
                }
            }
        }
    }

    @Override
    public void onWorldRender() {
        if(target != null) {
            AxisAlignedBB box = target.getRenderBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            GL11.glLineWidth(lineWidth.getValue());
            float r = (target.hurtTime > 0 ? 255 : color.getColor().getRed())  / 255f;
            float g = (target.hurtTime > 0 ? 0 : color.getColor().getRed())  / 255f;
            float b = (target.hurtTime > 0 ? 0 : color.getColor().getRed())  / 255f;
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, 1f);
            RenderUtil.release();
        }
    }

    public void attack(Entity entity) {
        mc.playerController.attackEntity(mc.player, entity);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        if(target != null)
            target = null;
    }
}
