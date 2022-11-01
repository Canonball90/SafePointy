package thefellas.safepoint;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import security.JSON;


@Mod(modid = SafepointMod.MOD_ID, name = SafepointMod.MOD_NAME, version = SafepointMod.VERSION)
public class SafepointMod {

    public static final String MOD_ID = "safepoint";
    public static final String MOD_NAME = "SafePoint";
    public static final String VERSION = "2.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Safepoint.INSTANCE.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Safepoint.hwidManager.processVerification();
        JSON.parseJson();
    }
}
