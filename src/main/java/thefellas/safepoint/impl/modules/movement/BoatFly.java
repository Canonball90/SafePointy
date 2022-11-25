package thefellas.safepoint.impl.modules.movement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import java.util.Comparator;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.item.EntityBoat;
import org.lwjgl.input.Keyboard;
import thefellas.safepoint.core.event.events.SendPacketEvent;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;
import thefellas.safepoint.impl.settings.impl.KeySetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "BoatFly", description = "Allows you to fly in a boat", category = Module.Category.Movement)
public class BoatFly extends Module {
    IntegerSetting speed = new  IntegerSetting ( "Speed" , 2 , 0 , 10 , this);
    IntegerSetting verticalSpeed = new  IntegerSetting ( "Speed" , 1 , 0 , 10 , this);
    KeySetting downKey = new KeySetting("Down Key", Keyboard.KEY_LCONTROL, this);
    IntegerSetting glideSpeed = new  IntegerSetting ( "Speed" , 2 , -10 , 10 , this);
    BooleanSetting staticY = new BooleanSetting("Static Y", true, this);
    BooleanSetting hover = new BooleanSetting("Hover", false, this);
    BooleanSetting bypass = new BooleanSetting("Bypass", false, this);
    BooleanSetting extraCalc = new BooleanSetting("Calculation", false, this);

    private int packetCounter;
    private boolean stopFlying;

    public BoatFly()
    {
        this.packetCounter = 0;
        this.stopFlying = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.mc.player.getRidingEntity() instanceof EntityBoat) {
            this.mc.player.getRidingEntity().setNoGravity(false);
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if (this.mc.world != null && this.mc.player.getRidingEntity() != null) {
            final Entity riding = this.mc.player.getRidingEntity();
            if (riding instanceof EntityBoat) {
                this.steerBoat(riding);
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(final SendPacketEvent p_Event) {
        if(nullCheck()) return;
        if (this.mc.world != null && this.mc.player != null) {
            if (this.mc.player.getRidingEntity() instanceof EntityBoat) {
                if (this.bypass.getValue() && p_Event.getPacket() instanceof CPacketInput && !this.mc.gameSettings.keyBindSneak.isKeyDown() && !this.mc.player.getRidingEntity().onGround) {
                    ++this.packetCounter;
                    if (this.packetCounter == 3) {
                        this.NCPPacketTrick();
                    }
                }
                if ((this.bypass.getValue() && p_Event.getPacket() instanceof SPacketPlayerPosLook) || (p_Event.getPacket() instanceof SPacketMoveVehicle && !this.stopFlying)) {
                    p_Event.setCanceled(true);
                }
            }
        }
    }

    private void steerBoat(final Entity boat) {
        if (this.extraCalc.getValue()) {
            boat.rotationYaw = this.mc.player.rotationYaw;
            boat.rotationPitch = this.mc.player.rotationPitch;
        }
        boat.setNoGravity(this.hover.getValue());
        int angle = 0;
        final boolean forward = this.mc.gameSettings.keyBindForward.isKeyDown();
        final boolean left = this.mc.gameSettings.keyBindLeft.isKeyDown();
        final boolean right = this.mc.gameSettings.keyBindRight.isKeyDown();
        final boolean back = this.mc.gameSettings.keyBindBack.isKeyDown();
        if (!forward || !back) {
            boat.motionY = 0.0;
        }
        if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            boat.motionY += this.verticalSpeed.getValue() / 2.0f;
        }
        if (this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            boat.motionY -= this.verticalSpeed.getValue() / 2.0f;
        }
        if (!forward && !left && !right && !back) {
            return;
        }
        if (left && right) {
            if (forward) {
                angle = 0;
            }
            else if (back) {
                angle = 180;
            }
        }
        else if (forward && back) {
            if (left) {
                angle = -90;
            }
            else if (right) {
                angle = 90;
            }
            else {
                angle = -1;
            }
        }
        else if (left) {
            angle = -90;
        }
        else if (right) {
            angle = 90;
        }
        else {
            angle = 0;
        }
        if (forward) {
            angle /= 2;
        }
        else if (back) {
            angle = 180 - angle / 2;
        }
        if (angle == -1) {
            return;
        }
        if (this.isBorderingChunk(boat, boat.motionX, boat.motionZ)) {
            this.stopFlying = true;
        }
        else {
            this.stopFlying = false;
        }
        final float yaw = this.mc.player.rotationYaw + angle;
        boat.motionX = this.getRelativeX(yaw) * this.speed.getValue();
        boat.motionZ = this.getRelativeZ(yaw) * this.speed.getValue();
    }

    private double getRelativeX(final float yaw) {
        return Math.sin(Math.toRadians(-yaw));
    }

    private double getRelativeZ(final float yaw) {
        return Math.cos(Math.toRadians(yaw));
    }

    private boolean isBorderingChunk(final Entity boat, final Double motX, final Double motZ) {
        return this.mc.world.getChunk((int)(boat.posX + motX) / 16, (int)(boat.posZ + motZ) / 16) instanceof EmptyChunk;
    }

    private void NCPPacketTrick() {
        this.packetCounter = 0;
        this.mc.player.getRidingEntity().dismountRidingEntity();
        final Entity l_Entity = (Entity)this.mc.world.loadedEntityList.stream().filter(p_Entity -> p_Entity instanceof EntityBoat).min(Comparator.comparing(p_Entity -> this.mc.player.getDistance(p_Entity))).orElse(null);
        if (l_Entity != null) {
            this.mc.playerController.interactWithEntity((EntityPlayer)this.mc.player, l_Entity, EnumHand.MAIN_HAND);
        }
    }

}
