package thefellas.safepoint.impl.modules;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.combat.*;
import thefellas.safepoint.impl.modules.combat.burrow.SelfBlock;
import thefellas.safepoint.impl.modules.core.*;
import thefellas.safepoint.impl.modules.misc.*;
import thefellas.safepoint.impl.modules.movement.*;
import thefellas.safepoint.impl.modules.player.*;
import thefellas.safepoint.impl.modules.player.scaffold.Scaffold;
import thefellas.safepoint.impl.modules.visual.*;
import thefellas.safepoint.impl.ui.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleInitializer {

    List<Module> moduleList;

    public ModuleInitializer(){
        moduleList = new ArrayList<>();
        init();
    }

    public void init(){

        //COMBAT
        moduleList.add(new AA_AutoCrystal());
        moduleList.add(new AB_Criticals());
        moduleList.add(new SelfBlock());
        moduleList.add(new AC_KillAura());
        moduleList.add(new AD_Hitbox());
        moduleList.add(new AE_BowSpam());
        moduleList.add(new AF_Replenish());
        moduleList.add(new AG_Surround());
        moduleList.add(new AutoArmor());
        moduleList.add(new AutoCrystal2b2t());
        moduleList.add(new AutoCrystalNew());
        moduleList.add(new AutoLog());
        moduleList.add(new AutoTrap());
        moduleList.add(new AutoWeb());
        moduleList.add(new BedAura());
        moduleList.add(new BowAim());
        moduleList.add(new HoleFill());
        moduleList.add(new Offhand());
        moduleList.add(new Quiver());

        //MOVEMENT
        moduleList.add(new AA_ElytraFly());
        moduleList.add(new AB_Strafe());
        moduleList.add(new AC_Spider());
        moduleList.add(new AD_Sprint());
        moduleList.add(new AE_NoFall());
        moduleList.add(new AF_Flight());
        moduleList.add(new AH_Step());
        moduleList.add(new AntiVoid());
        moduleList.add(new BoatFly());
        moduleList.add(new FastWeb());
        moduleList.add(new InventoryMove());
        moduleList.add(new LongJump());
        moduleList.add(new NoSlow());
        moduleList.add(new NoSlowBypass());
        moduleList.add(new OldfagNoFall());
        moduleList.add(new PacketFly());
        moduleList.add(new Parkour());
        moduleList.add(new ReverseStep());
        moduleList.add(new TunnelSpeed());

        //PLAYER
        moduleList.add(new AA_PacketEXP());
        moduleList.add(new Scaffold());
        moduleList.add(new AB_Godmode());
        moduleList.add(new AC_BigPOV());
        moduleList.add(new AD_PacketMine());
        moduleList.add(new AE_MiddleClickPearl());
        moduleList.add(new MultiTask());
        moduleList.add(new Nuker());
        moduleList.add(new PacketEat());
        moduleList.add(new PacketSwing());
        moduleList.add(new PearlAlert());
        moduleList.add(new DupeShulker5b5t());

        //RENDER
        moduleList.add(new AA_Trajectories());
        moduleList.add(new AB_JumpCircles());
        moduleList.add(new AD_FullBright());
        moduleList.add(new AE_EntityESP());
        moduleList.add(new AF_HoleESP());
        moduleList.add(new AG_GlowESP());
        moduleList.add(new AH_ItemESP());
        moduleList.add(new AI_Trails());
        moduleList.add(new AK_BlockObject());
        moduleList.add(new AL_ThirdPerson());
        moduleList.add(new AntiWeather());
        moduleList.add(new AssESP());
        moduleList.add(new Chams());
        moduleList.add(new Crosshair());
        moduleList.add(new ESP());
        moduleList.add(new NameTags());
        moduleList.add(new NoBob());
        moduleList.add(new Ruler());
        moduleList.add(new RuneScapeChat());
        moduleList.add(new SignNametags());
        moduleList.add(new SmallHand());
        moduleList.add(new ViewModel());
        moduleList.add(new WallHack());

        //MISC
        moduleList.add(new AA_AutoRespawn());
        moduleList.add(new AB_FakePlayer());
        moduleList.add(new AC_ChatSuffix());
        moduleList.add(new AD_AntiRegear());
        moduleList.add(new Australia());
        moduleList.add(new AuthBypass());
        moduleList.add(new AutoExcuse());
        moduleList.add(new ChestSearchBar());
        moduleList.add(new HitSound());
        moduleList.add(new ModuleAutoGhastFarmer());
        moduleList.add(new PosDesync());

        //CORE
        moduleList.add(new AA_ChatNotifications());
        moduleList.add(new AB_HudEditor());
        moduleList.add(new AC_ClickGui());
        moduleList.add(new AD_PARTS());
        moduleList.add(new AD_Stats());
        moduleList.add(new AntiLog4j());
        moduleList.add(new BetterFPS());
        moduleList.add(new GUIBlur());
        moduleList.add(new MainMenu());
        moduleList.add(new MCF());
        moduleList.add(new Notification());
        moduleList.add(new StreamerMode());
        moduleList.add(new CustomFont());
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public List<Module> getCategoryModules(Module.Category category) {
        final List<Module> list = new ArrayList<>();
        for (Module module : moduleList) {
            if (module.getCategory() == category)
                list.add(module);
        }

        return list;
    }

    public void addModules(String folder) {
        try {
            final List<Class<?>> classes = ClassFinder.from("thefellas.safepoint.impl.modules." + folder);
            if (classes == null)
                return;
            for (Class<?> clazz : classes) {
                if (!Modifier.isAbstract(clazz.getModifiers()) && Module.class.isAssignableFrom(clazz)) {
                    for (Constructor<?> constructor : clazz.getConstructors()) {
                        final Module instance = (Module) constructor.newInstance();
                        for (Field field : instance.getClass().getDeclaredFields())
                            if (!field.isAccessible())
                                field.setAccessible(true);

                        moduleList.add(instance);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Module> getEnabledModules(){
        List<Module> enabled = new ArrayList<>();
        for (Module module : moduleList) {
            if(module.isEnabled()) enabled.add(module);
        }

        return enabled;
    }

    public void onTick(){
        for(Module module : moduleList){
            if(module.isEnabled())
                module.onTick();
        }
    }

    public void onWorldRender(){
        moduleList.forEach(Module::onWorldRender);
    }

    public void onKey(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || Safepoint.mc.currentScreen instanceof ClickGui)
            return;

        moduleList.forEach(module -> {
            if (module.getKeyBind() == eventKey) {
                if (module.isEnabled())
                    module.disableModule();
                else module.enableModule();
            }
        });
    }
}
