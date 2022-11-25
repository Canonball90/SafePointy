package thefellas.safepoint.impl.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.client.event.InputUpdateEvent;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.ui.clickgui.ClickGui;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@ModuleInfo(name = "InventoryMove", description = "Allows you to move while in your inventory", category = Module.Category.Movement)
public class InventoryMove extends Module {

    BooleanSetting sneak = new BooleanSetting("Sneak", false, this);

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (!(event.getMovementInput() instanceof MovementInputFromOptions) || isValidGui(mc.currentScreen)) return;

        event.getMovementInput().moveStrafe = 0.0f;
        event.getMovementInput().moveForward = 0.0f;

        try {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                ++event.getMovementInput().moveForward;
                event.getMovementInput().forwardKeyDown = true;
            } else {
                event.getMovementInput().forwardKeyDown = false;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                --event.getMovementInput().moveForward;
                event.getMovementInput().backKeyDown = true;
            } else {
                event.getMovementInput().backKeyDown = false;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                ++event.getMovementInput().moveStrafe;
                event.getMovementInput().leftKeyDown = true;
            } else {
                event.getMovementInput().leftKeyDown = false;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                --event.getMovementInput().moveStrafe;
                event.getMovementInput().rightKeyDown = true;
            } else {
                event.getMovementInput().rightKeyDown = false;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                event.getMovementInput().jump = true;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && sneak.getValue()) {
                event.getMovementInput().sneak = true;
            }

            if ((Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()) && canSprint())) {
                mc.player.setSprinting(true);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidGui(GuiScreen screen) {
        return screen == null || screen instanceof GuiChat || screen instanceof GuiEditSign || screen instanceof GuiRepair || screen instanceof ClickGui;
    }

    public static boolean canSprint() {
        if (mc.player.moveForward != 0 && !mc.gameSettings.keyBindBack.isKeyDown() && mc.player.getFoodStats().getFoodLevel() > 6 && !mc.player.collidedHorizontally) {
            return !mc.player.isSprinting();
        }
        return false;
    }

}
