package thefellas.safepoint.impl.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.core.initializers.NotificationManager;

import java.util.List;

public class ReloadSoundCommand extends Command {
    public ReloadSoundCommand() {
        super("ReloadSound", "Reloads the sound file", ".ReloadSound");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            SoundManager sndManager = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), new String[]{"sndManager", "field_147694_f"});
            sndManager.reloadSoundSystem();
            NotificationManager.sendMessage("Ok", "Reloaded Sound System.");
        } catch (Exception e) {
            System.out.println("Could not restart sound manager: " + e.toString());
            e.printStackTrace();
            NotificationManager.sendMessage("Error", "Could not restart sound manager: " + e.toString());
        }
    }
}
