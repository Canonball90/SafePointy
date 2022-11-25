package thefellas.safepoint.impl.modules.core;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "AntiLog4j", description = "Anti Log4j", category = Module.Category.Core)
public class AntiLog4j extends Module {
    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent e) {
        if (e.getMessage().getUnformattedText().contains("${") || e.getMessage().getUnformattedText().contains("$<") || e.getMessage().getUnformattedText().contains("$:-") || e.getMessage().getUnformattedText().contains("jndi:ldap")) {
            e.setCanceled(true);
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[SafePoint] " + "Log4j detected, you saved from hacking!"), 1);
        }
    }
}
