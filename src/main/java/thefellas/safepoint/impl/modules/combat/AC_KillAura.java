package thefellas.safepoint.impl.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.ColorSetting;
import thefellas.safepoint.impl.settings.impl.DoubleSetting;
import thefellas.safepoint.impl.settings.impl.FloatSetting;
import thefellas.safepoint.core.utils.InterpolationUtil;
import thefellas.safepoint.core.utils.RenderBuilder;
import thefellas.safepoint.core.utils.RenderUtil;


import java.awt.*;

@ModuleInfo(name = "KillAura", description = "Attacks entities around you", category = Module.Category.Combat)
public class AC_KillAura extends Module {

    DoubleSetting range = new DoubleSetting("Range", 4, 1, 6, this);
    BooleanSetting players = new BooleanSetting("Players", true, this);
    BooleanSetting animals = new BooleanSetting("Animals", true, this);
    BooleanSetting mobs = new BooleanSetting("Mobs", true, this);

    FloatSetting lineWidth = new FloatSetting("LineWidth", 1.5f, 0f, 5f, this);
    FloatSetting Width = new FloatSetting("Width", 1.5f, -5f, 5f, this);
    ColorSetting color = new ColorSetting("Color", new Color(0, 255, 110, 169), this);

    public EntityLivingBase target = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
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
            RenderUtil.drawCircle(new RenderBuilder()
                    .setup()
                    .line(lineWidth.getValue())
                    .depth(true)
                    .blend()
                    .texture(), InterpolationUtil.getInterpolatedPosition(target, 1), target.width - Width.getValue(), target.height * (0.5 * (Math.sin((mc.player.ticksExisted * 3.5) * (Math.PI / 180)) + 1)), color.getValue());
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
