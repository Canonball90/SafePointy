package thefellas.safepoint.modules.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.event.events.MoveEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;

@ModuleInfo(name = "LongJump", description = "LongJump", category = Module.Category.Movement)
public class LongJump extends Module {
    BooleanSetting packet = new BooleanSetting("Packet", false, this);
    IntegerSetting speed = new IntegerSetting("Speed", 30, 1, 100, this);

    private boolean jumped = false;
    private boolean boostable = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (nullCheck()) return;

        if (jumped)
        {
            if (mc.player.onGround || mc.player.capabilities.isFlying)
            {
                jumped = false;

                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;

                if (packet.getValue())
                {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
                }

                return;
            }

            if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f)) return;
            double yaw = getDirection();
            mc.player.motionX = -Math.sin(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? (speed.getValue() / 10f) : 1f));
            mc.player.motionZ = Math.cos(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? (speed.getValue() / 10f) : 1f));

            boostable = false;
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event)
    {
        if (nullCheck()) return;

        if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f) && jumped)
        {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            event.setX(0);
            event.setY(0);
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event)
    {
        if ((mc.player != null && mc.world != null) && event.getEntity() == mc.player && (mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f))
        {
            jumped = true;
            boostable = true;
        }
    }

    private double getDirection()
    {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0f) rotationYaw += 180f;

        float forward = 1f;

        if (mc.player.moveForward < 0f) forward = -0.5f;
        else if (mc.player.moveForward > 0f) forward = 0.5f;

        if (mc.player.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (mc.player.moveStrafing < 0f) rotationYaw += 90f * forward;

        return Math.toRadians(rotationYaw);
    }
}
