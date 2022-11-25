package thefellas.safepoint.impl.modules.visual;

import net.minecraft.client.Minecraft;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;
import thefellas.safepoint.core.utils.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ModuleInfo(name = "RuneScapeChat", category = Module.Category.Visual, description = "Makes the chat look like RuneScape.")
public class RuneScapeChat extends Module {
    public List<Player> playerList = new ArrayList<>();
    IntegerSetting scale = new IntegerSetting("Scale", 2, 1, 5, this);
    IntegerSetting timeToRemove = new IntegerSetting("Time to remove", 1, 0, 5, this);

    @SubscribeEvent
    public void onChatR(ClientChatReceivedEvent event) {
        String sender = StringUtils.substringBetween(event.getMessage().getFormattedText(), "<", ">");
        EntityPlayer entityPlayer = mc.world.getPlayerEntityByName(sender);
        if (entityPlayer == null) return;
        if (entityPlayer.equals(mc.player)) return;
        for (Player p : playerList) {
            if (p.sender.equals(entityPlayer)) {
                p.messageMap.put(event.getMessage().getFormattedText().replace("<" + sender + ">", ""), System.currentTimeMillis());
                return;
            }
        }
        Player player = new Player(entityPlayer);
        playerList.add(player);
        for (Player player1 : playerList) {
            if (player1.sender.equals(player.sender)) {
                player1.messageMap.put(event.getMessage().getFormattedText().replace("<" + sender + ">", ""), System.currentTimeMillis());
                return;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        if (playerList.isEmpty()) return;
        for (Player player : playerList) {
            if (player.messageMap.isEmpty()) {
                playerList.remove(player);
                return;
            }
            int yOffset = 0;
            for (Map.Entry<String, Long> message : player.messageMap.entrySet()) {
                if (System.currentTimeMillis() - message.getValue() >= timeToRemove.getValue() * 1000) {
                    player.messageMap.remove(message.getKey());
                    continue;
                }
                double yAdd = player.sender.isSneaking() ? 1.75 : 2.25;
                double deltaX = MathHelper.clampedLerp(player.sender.lastTickPosX, player.sender.posX, event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(player.sender.lastTickPosY, player.sender.posY, event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(player.sender.lastTickPosZ, player.sender.posZ, event.getPartialTicks());
                Vec3d projection = RenderUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                //Render stuff
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                mc.fontRenderer.drawStringWithShadow(message.getKey(), -(mc.fontRenderer.getStringWidth(message.getKey()) / 2f), -(mc.fontRenderer.FONT_HEIGHT) - yOffset, new Color(255, 255, 0).getRGB());
                GlStateManager.popMatrix();
                yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
            }
        }
    }

    public static class Player {
        private final EntityPlayer sender;
        public ConcurrentHashMap<String, Long> messageMap;

        public Player(EntityPlayer sender) {
            this.sender = sender;
            messageMap = new ConcurrentHashMap<>();
        }
    }
}
