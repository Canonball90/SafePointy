package thefellas.safepoint;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thefellas.safepoint.proxy.ServerProxy;


@Mod(modid = SafepointMod.MOD_ID, name = SafepointMod.MOD_NAME, version = SafepointMod.VERSION)
public class SafepointMod {

    public static final String MOD_ID = "safepoint";
    public static final String MOD_NAME = "SafePoint";
    public static final String VERSION = "2.0";
    public static final String CLIENT_PROXY = "thefellas.safepoint.proxy.ClientProxy";
    public static final String SERVER_PROXY = "thefellas.safepoint.sproxy.ServerProxy";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Safepoint.INSTANCE.init();
        proxy.init(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }


    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static ServerProxy proxy;

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //Safepoint.hwidManager.processVerification();
        proxy.postInit(event);
    }
}
