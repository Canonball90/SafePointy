package thefellas.safepoint.impl.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "WallHack", description = "Allows you to see through walls", category = Module.Category.Visual)
public class WallHack extends Module {
    public static WallHack INSTANCE;
    BooleanSetting targetPlayer = new BooleanSetting("Target Player", true, this);
    BooleanSetting friend = new BooleanSetting("Friend", true, this);
    BooleanSetting targetHostile = new BooleanSetting("Target Hostile", true, this);
    BooleanSetting targetPassive = new BooleanSetting("Target Passive", true, this);
    BooleanSetting rustme = new BooleanSetting("Rustme", true, this);

    public WallHack() {
        WallHack.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    void render(final Entity entity, final float ticks) {
        try {
            if (entity == null || entity == Minecraft.getMinecraft().player) {
                return;
            }
            if (entity == Minecraft.getMinecraft().getRenderViewEntity() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                return;
            }
            Minecraft.getMinecraft().entityRenderer.disableLightmap();
            Minecraft.getMinecraft().getRenderManager().renderEntityStatic(entity, ticks, false);
            Minecraft.getMinecraft().entityRenderer.enableLightmap();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        for (final Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (this.targetPlayer.getValue() && entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().getRenderViewEntity()) {
                if (Safepoint.friendInitializer.isFriend(entity.getName()) && this.friend.getValue()) {
                    this.render(entity, event.getPartialTicks());
                }
                else {
                    this.render(entity, event.getPartialTicks());
                }
            }
            else if (this.targetHostile.getValue() && entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
                this.render(entity, event.getPartialTicks());
            }
            else if (this.targetPassive.getValue() && (entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false) || entity.isCreatureType(EnumCreatureType.CREATURE, false))) {
                this.render(entity, event.getPartialTicks());
            }
            else {
                if (!this.rustme.getValue() || !(entity instanceof EntityArmorStand)) {
                    continue;
                }
                this.render(entity, event.getPartialTicks());
            }
        }
    }
}
