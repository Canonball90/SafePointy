package thefellas.safepoint.impl.modules.player;

import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.EnumSetting;
import thefellas.safepoint.impl.settings.impl.FloatSetting;
import thefellas.safepoint.impl.settings.impl.KeySetting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Arrays;

@ModuleInfo(name = "PacketEXP", category = Module.Category.Player, description = "Throws Exp using packets")
public class AA_PacketEXP extends Module {

    public EnumSetting triggerMode = new EnumSetting("TriggerMode", "RightClick", Arrays.asList("RightClick", "MiddleClick", "Custom"), this);
    public KeySetting customKey = new KeySetting("CustomKey", Keyboard.KEY_NONE, this, v -> triggerMode.getValue().equals("Custom"));
    public FloatSetting packets = new FloatSetting("Packets", 2f, 0f, 10f, this);
    public BooleanSetting onlyInHand = new BooleanSetting("OnlyInHand", false, this);

    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (triggerMode.getValue().equals("RightClick") && !mc.gameSettings.keyBindUseItem.isKeyDown())
            return;

        if (triggerMode.getValue().equals("MiddleClick") && !Mouse.isButtonDown(2))
            return;

        if (triggerMode.getValue().equals("Custom") && !Keyboard.isKeyDown(customKey.getKey()))
            return;

        if (onlyInHand.getValue() && !mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE))
            return;

        if (getItemHotbar(Items.EXPERIENCE_BOTTLE) == -1)
            return;

        mc.player.connection.sendPacket(new CPacketHeldItemChange(getItemHotbar(Items.EXPERIENCE_BOTTLE)));

        for (int i = 0; i < packets.getValue(); i++) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    public int getItemHotbar(Item item) {
        int itemSlot = -1;
        for(int i = 9; i > 0; --i)
            if(mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        return itemSlot;
    }
}
